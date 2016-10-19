package yuan.com.luoling.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 视频播放服务类
 * Created by YUAN on 2016/10/13.
 */

public class VideoServices extends Service {
    private final String TAG = "VideoServices";

    /**
     * 实例化一个不可改变的media play，用于播放视频
     */
    private final MediaPlayer player = new MediaPlayer();
    /**
     * 实例化bind的对象
     */
    private VideoBind videoBind = new VideoBind();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return videoBind;
    }

    public class VideoBind extends Binder {
        public Service getServices() {
            return VideoServices.this;
        }
    }
    public MediaPlayer getPlayer() {
        return player;
    }
}
