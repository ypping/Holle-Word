package yuan.com.luoling.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.MusicFiles;

/**
 * 音乐服务类
 * Created by YUAN on 2016/9/28.
 */

public class MusicServices extends Service {
    private final String TAG = "MusicServices";
    private Context context;
    /**
     * 服务的对象
     */
    private MusicBinder musicBinder = new MusicBinder();
    /**
     * media play播放对象
     */
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    /**
     * 是否播放
     */
    private boolean isPlay = false;
    /**
     * 当前的音乐集合
     */
    private List<MusicFiles> musicFiles = ListDate.getListData().getMusicFiles();
    /**
     * 播放的歌曲的当前位置
     */
    private int playPosition;
    /**
     * 播放的歌曲在列表中的位置
     */
    private int position;
    /**
     * 音乐接口
     */
    private MusicListener musicListener;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    public class MusicBinder extends Binder {
        public Service getService() {
            return MusicServices.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public int getPosition() {
        return position;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    /**
     * 创建一个播放服务，在播放完成后终结
     *
     * @return
     */
    private MediaPlayer mediaCreate() {
       /* if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
        }*/
        mediaPlayer.reset();
        mediaPlayer.setOnErrorListener(errorListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        return mediaPlayer;
    }

    /**
     * 设置音乐
     *
     * @param position 位置
     * @throws IOException 将会抛出一个播放异常
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setPlayMusic(int position, Context context) throws IOException {
        this.context = context;
        if (this.position != position && isPlay) {
            stopMusic();
            this.position = position;
            mediaCreate();
            playMusic(musicFiles.get(position).getPath(), null, context);
            return;
        }

        if (!isPlay) {
            isPlay = true;
            playMusic(musicFiles.get(position).getPath(), null, context);
        } else if (mediaPlayer != null) {
            if (this.position != position) {
                this.position = position;
                mediaCreate();
                playMusic(musicFiles.get(position).getPath(), null, context);
            } else {

                startMusic();
            }
        } else {
            mediaCreate();
            playMusic(musicFiles.get(position).getPath(), null, context);
        }
    }

    public void setPlayMusic(Uri uri, Context context)
            throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        mediaPlayer.setDataSource(context, uri);
        mediaPlayer.prepare();
    }


    /**
     * 连续播放下一首
     *
     * @return 返回一个media play的实例
     */
    private MediaPlayer NextMusic(Context context) {
        position += position;
        //  mediaPlayer = MediaPlayer.create(context, Uri.parse(musicFiles.get(position).getPath()));
        mediaPlayer.reset();
        mediaPlayer.start();
        mediaPlayer.release();

        return mediaPlayer;
    }

    /**
     * 播放音乐
     *
     * @throws IOException 将会抛出一个播放异常
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setPlayMusic(int prepaerd) throws IOException {
        mediaPlayer.reset();// 把各项参数恢复到初始状态
        /**
         *  通过MediaPlayer.setDataSource() 的方法,将URL或文件路径以字符串的方式传入.使用setDataSource ()方法时,要注意以下三点:
         1.构建完成的MediaPlayer 必须实现Null 对像的检查.
         2.必须实现接收IllegalArgumentException 与IOException 等异常,在很多情况下,你所用的文件当下并不存在.
         3.若使用URL 来播放在线媒体文件,该文件应该要能支持pragressive 下载.
         */
        mediaPlayer.setDataSource(musicFiles.get(prepaerd).getPath());
        mediaPlayer.prepare();// 进行缓冲
        mediaPlayer.setOnPreparedListener(new PreparedListener(playPosition));
    }

    MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mp.reset();
            return false;
        }
    };

    /**
     * 上一首
     */
    public void leftMusic(int position) {
        if (null == mediaPlayer) {
            new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            if (position >= 0) {
                this.position = position;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(musicFiles.get(position).getPath());
                    musicListener.runDirection(musicFiles.get(position).getLrcURL());
                    mediaPlayer.prepare();
                    startMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e(TAG, TAG + "left" + musicFiles.get(position).getLrcURL());
            musicListener.runDirection(musicFiles.get(position).getLrcURL());
        }
    }

    /**
     * 下一首
     *
     * @param position
     */
    public void rightMusic(int position) {
        if (null == mediaPlayer) {
            mediaCreate();
        } else if (mediaPlayer.isPlaying()) {
            if (position < musicFiles.size()) {
                this.position = position;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(musicFiles.get(position).getPath());
                    musicListener.runDirection(musicFiles.get(position).getLrcURL());
                    mediaPlayer.prepare();
                    startMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            musicListener.runDirection(musicFiles.get(position).getLrcURL());
            Log.e(TAG, TAG + "right" + musicFiles.get(position).getLrcURL());
        }
    }

    public void playMusic(@Nullable String path, @Nullable Uri uri, Context context) {
        if (null == mediaPlayer) {
            mediaCreate();
        }
        try {
            Log.e(TAG, TAG + "path:" + path + "url:" + uri);
            if (path != null) {
                mediaPlayer.setDataSource(path);
            } else if (uri != null) {
                mediaPlayer.setDataSource(context, uri);
            } else {
                Toast.makeText(context, "没有路径", Toast.LENGTH_SHORT).show();
            }

            mediaPlayer.prepare();
            mediaPlayer.start();
            musicListener.playMusic(mediaPlayer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     */
    public void pauseMusic() {
        Log.e(TAG, TAG + mediaPlayer.isPlaying());
        mediaPlayer.pause();
        musicListener.pauseMusic();
    }

    /**
     * 继续播放音乐
     */
    public void startMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            musicListener.goOnMusic(playPosition, mediaPlayer);
        } else {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(musicFiles.get(position).getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                musicListener.goOnMusic(playPosition, mediaPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, TAG + "path:" + musicFiles.get(position).getPath());
    }

    /**
     * 停止音乐
     *
     * @throws IOException
     */
    public void stopMusic() throws IOException {
        if (null == mediaPlayer) {
            mediaPlayer.release();
        } else if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            musicListener.stopMusic();
        } else {
            mediaPlayer.reset();
            //  musicListener.stopMusic();
        }
    }

    /**
     * 只有电话来了之后才暂停音乐的播放
     */
    private final class MyPhoneListener extends android.telephony.PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://电话来了
                    if (mediaPlayer.isPlaying()) {
                        playPosition = mediaPlayer.getCurrentPosition();// 获得当前播放位置
                        try {
                            stopMusic();
                            musicListener.stopMusic();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE: //通话结束
                    if (playPosition > 0 && musicFiles.get(playPosition).getPath() != null) {
                        try {
                            setPlayMusic(playPosition);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        playPosition = 0;
                    }
                    break;
            }

        }
    }

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int playPosition;

        public PreparedListener(int position) {
            this.playPosition = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if (playPosition > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(playPosition);
            }
        }
    }

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.stop();
            mp.release();
        }
    };

    public void setMusicListener(MusicListener musicListener) {
        this.musicListener = musicListener;
    }

    /**
     * 音乐服务与歌词文件的接口，服务主要做控制器的作用，歌词LrcView做显示功能
     */
    public interface MusicListener {
        /**
         * 播放的方法
         */
        void playMusic(MediaPlayer mediaPlayer);

        /**
         * 暂停的方法
         */
        void pauseMusic();

        /**
         * 停止的方法
         */
        void stopMusic();

        /**
         * 继续的方法
         */
        void goOnMusic(int time, MediaPlayer mediaPlayer);

        /**
         * 播放音乐是上一首还是下一首
         */
        void runDirection(String path);

    }
}
