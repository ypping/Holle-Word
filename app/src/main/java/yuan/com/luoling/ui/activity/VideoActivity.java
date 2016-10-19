package yuan.com.luoling.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.VideoView;

import java.util.List;

import yuan.com.luoling.ActivityManagement;
import yuan.com.luoling.R;
import yuan.com.luoling.bean.VideoFiles;

/**
 * 视频activity
 * Created by YUAN on 2016/9/19.
 */
public class VideoActivity extends Activity {
    private VideoView videoView;
    private List<VideoFiles> list;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ActivityManagement.getInstance().sendActivity.createActivity(this);
        Intent intent = getIntent();
        list = (List<VideoFiles>) intent.getSerializableExtra("VideoFiles");
        position = intent.getIntExtra("position", 0);
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        videoView = (VideoView) findViewById(R.id.mainVideo);
        videoView.setVideoPath(list.get(position).getPath());
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        videoView.setFitsSystemWindows(true);
        videoView.setHovered(true);
        videoView.setTop(55);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagement.getInstance().sendActivity.destroyActivity(this);
    }
}
