package yuan.com.luoling.db;

import android.util.Log;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.bean.VideoFiles;

/**
 * Created by yuan-pc on 2016/06/29.
 */
public class MyDbService {
    private final String TAG = "MyDbService";
    private DbManager dbManager = MyApplication.getApp().getDbManager();
    private static MyDbService myDbService;

    public static MyDbService getInterest() {

        if (myDbService == null) {
            myDbService = new MyDbService();
        }
        return myDbService;
    }

    /**
     * 添加图片到数据库的方法
     *
     * @param lists 图片集合
     * @throws DbException
     */
    public void addImageDB(List<ImageFiles> lists) throws DbException {
        for (ImageFiles imageFiles : lists) {
            if (imageFiles.getPath() != null)
                dbManager.save(imageFiles);
            Log.e("DBService", "图片数据库");
        }
        //showDbMessage("【dbAdd】第一个对象:" + lists.get(0).toString());//user的主键Id不为0
    }

    /**
     * 添加图片到数据库的方法
     *
     * @param lists 图片集合
     * @throws DbException
     */
    public void addMusicDB(List<MusicFiles> lists) throws DbException {
        for (MusicFiles musicFiles : lists) {
            if (musicFiles.getPath() != null)
                dbManager.save(musicFiles);
            Log.e("DBService", "音乐数据库");
        }
        //showDbMessage("【dbAdd】第一个对象:" + lists.get(0).toString());//user的主键Id不为0
    }

    /**
     * 添加图片到数据库的方法
     *
     * @param lists 图片集合
     * @throws DbException
     */
    public void addVideoDB(List<VideoFiles> lists) throws DbException {
        for (VideoFiles videoFiles : lists) {
            if (videoFiles.getPath() != null)
                dbManager.save(videoFiles);
            Log.e("DBService", "视频数据库");
        }
        //showDbMessage("【dbAdd】第一个对象:" + lists.get(0).toString());//user的主键Id不为0
    }

    public void updateImageDB(List<ImageFiles> lists) throws DbException {

        dbManager.replace(lists);
        //如果一个对象主键为null则会新增该对象,成功之后【会】对user的主键进行赋值绑定,否则根据主键去查找更新
    }

    public void updateMusicDB(List<MusicFiles> lists) throws DbException {

        dbManager.replace(lists);
        //如果一个对象主键为null则会新增该对象,成功之后【会】对user的主键进行赋值绑定,否则根据主键去查找更新
    }

    /**
     * 移除数据库中此图片文件
     *
     * @param imageFiles
     * @throws DbException
     */
    public void removeImage(ImageFiles imageFiles) throws DbException {
        dbManager.delete(imageFiles);
    }
}
