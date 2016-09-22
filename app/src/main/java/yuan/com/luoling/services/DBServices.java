package yuan.com.luoling.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.bean.VideoFiles;
import yuan.com.luoling.db.DBCursorUtils;

/**
 * 数据库服务
 * Created by YUAN on 2016/9/8.
 */
public class DBServices extends Service {
    private ImageBinder imageBinder = new ImageBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return imageBinder;
    }

    /**
     * 一个继承binder的类，实现自己的方法
     */
    public class ImageBinder extends Binder {
        public Service getServices() {
            return DBServices.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 第一次执行app，调用递归去实现遍历
     *
     * @param imageFiles 图片集合
     */
    public List<ImageFiles> fistApp(List<ImageFiles> imageFiles) {
        return DBCursorUtils.findFilePath4(Environment.getExternalStorageDirectory().getPath(), imageFiles);
    }

    /**
     * 数据库添加图片
     */
    public List<ImageFiles> curcorImage(Context context) {
        return DBCursorUtils.addImage(DBCursorUtils.findImage(context));
    }

    /**
     * 向数据库添加音乐文件
     *
     * @param context 上下文
     * @return MusicFiles集合
     */
    public List<MusicFiles> curcorMusic(Context context) {
        return DBCursorUtils.addMusic(DBCursorUtils.findMusic(context));
    }

    /**
     * 向数据库添加视频资源
     *
     * @param context 查找数据库需要上下文做引导
     * @return VideoFiles集合
     */
    public List<VideoFiles> curcorVideo(Context context) {
        return DBCursorUtils.addVideo(DBCursorUtils.findVideo(context));
    }
}
