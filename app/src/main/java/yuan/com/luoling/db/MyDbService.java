package yuan.com.luoling.db;

import android.util.Log;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.bean.ImageFile;

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

    public void addImageDB(List<ImageFile> lists) throws DbException {
        for (ImageFile imageFile : lists) {
            if (imageFile.getPath() != null)
                dbManager.save(imageFile);
            Log.e("DBService", "数据库");
        }

        //showDbMessage("【dbAdd】第一个对象:" + lists.get(0).toString());//user的主键Id不为0
    }

    public void updateImageDB(List<ImageFile> lists) throws DbException {

        dbManager.saveOrUpdate(lists);
        //如果一个对象主键为null则会新增该对象,成功之后【会】对user的主键进行赋值绑定,否则根据主键去查找更新
    }
}
