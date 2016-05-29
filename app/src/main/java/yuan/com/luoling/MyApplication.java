package yuan.com.luoling;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import org.xutils.DbManager;
import org.xutils.x;


/**
 * Created by yuan-pc on 2016/05/23.
 */
public class MyApplication extends Application {
    private static Context context;
    private static MyApplication myApplication;
    private DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        myApplication = this;
        context = this;
        // x.Ext.setDebug(BuildConfig.DEBUG);
        daoConfig = new DbManager.DaoConfig()
                .setDbName("LL_db")//创建数据库的名称
                .setDbVersion(1)//数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });
    }

    public static MyApplication getApp() {
        return myApplication;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return super.getApplicationInfo();
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
}
