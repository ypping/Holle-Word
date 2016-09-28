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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.bean.MusicFiles;

/**
 * Created by YUAN on 2016/9/28.
 */

public class MusicServices extends Service {
    private final String TAG = "MusicServices";
    /**
     * 服务的对象
     */
    private MusicBinder musicBinder = new MusicBinder();
    /**
     * media play播放对象
     */
    private MediaPlayer mediaPlayer;
    /**
     * 是否播放
     */
    private boolean isPlay = false;
    /**
     * 当前的音乐集合
     */
    private List<MusicFiles> musicFiles = new ArrayList<>();
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
    /**
     * 电话挂断的监听
     */
    public PhoneDropped phoneDropped;

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

    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
        setPhoneDropped(new PhoneDropped() {
            @Override
            public void dropped() {
                if (playPosition > 0 && musicFiles.get(position).getPath() != null) {
                    musicListener.runDirection(musicFiles.get(position).getPath(), playPosition);
                    playPosition = 0;
                }
            }
        });
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
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    /**
     * 播放音乐
     *
     * @param list     音乐列表
     * @param position 位置
     * @throws IOException 将会抛出一个播放异常
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void playMusic(List<MusicFiles> list, int position) {

        musicFiles = list;
        this.position = position;
        musicListener.playMusic(mediaPlayer, list.get(position).getPath());
        mediaPlayer.start();
    }

    /**
     * 连续播放下一首
     *
     * @return 返回一个media play的实例
     */
    private MediaPlayer NextMusic(Context context) {
        position += position;
        mediaPlayer = MediaPlayer.create(context, Uri.parse(musicFiles.get(position).getPath()));

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
    public void playMusic(int prepaerd) throws IOException {
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

        } else if (mediaPlayer.isPlaying()) {
            if (position >= 0) {
                this.position = position;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(musicFiles.get(position).getPath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下一首
     *
     * @param position
     */
    public void rightMusic(int position) {
        if (null == mediaPlayer) {

        } else if (mediaPlayer.isPlaying()) {
            if (position < musicFiles.size()) {
                this.position = position;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(musicFiles.get(position).getPath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 暂停音乐
     */
    public void pauseMusic() {
        musicListener.pauseMusic();
    }

    /**
     * 继续播放音乐
     */
    public void startMusic() {
        musicListener.goOnMusic();
    }

    /**
     * 停止音乐
     *
     * @throws IOException
     */
    public void stopMusic() throws IOException {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
            mediaPlayer.reset();
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
                        mediaPlayer.stop();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE: //通话结束
                    if (playPosition > 0 && musicFiles.get(playPosition).getPath() != null) {
                        try {
                            playMusic(playPosition);
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
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if (positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
        }
    }


    public void setPhoneDropped(PhoneDropped phoneDropped) {
        this.phoneDropped = phoneDropped;
    }

    /**
     * 电话挂断的一个监听
     */
    public interface PhoneDropped {
        void dropped();
    }

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
        void playMusic(MediaPlayer mediaPlayer, String path);

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
        void goOnMusic();

        /**
         * 播放音乐是上一首还是下一首
         */
        void runDirection(String path, int playPosition);
    }

}
