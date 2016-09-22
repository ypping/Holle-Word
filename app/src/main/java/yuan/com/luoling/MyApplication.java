package yuan.com.luoling;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import yuan.com.luoling.services.DBServices;


/**
 * Created by yuan-pc on 2016/05/23.
 */
public class MyApplication extends Application {
    private final String TAG = "MyApplication";
    private static Context context;
    private static MyApplication myApplication;
    private DbManager.DaoConfig daoConfig;
    private DbManager dbManager;
    private DBServices dbServices;
    /**
     * 是否是第一次运行
     */
    private boolean isFristRun;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        myApplication = this;
        context = this;
        x.Ext.setDebug(BuildConfig.DEBUG);
        daoConfig = new DbManager.DaoConfig()
                .setDbName("LuoLing_DB")//创建数据库的名称
                .setDbVersion(1)//数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        Log.i(TAG, TAG + db.getDatabase().getPath() + " 数据库被更新了 " + newVersion);
                    }
                })
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        Log.i(TAG, TAG + db.getDatabase().getPath() + "数据库被打开了  ");
                    }
                })
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i(TAG, TAG + db.getDatabase().getPath() + " 数据库创建了 " + table.getName());
                    }
                });
        dbManager = x.getDb(daoConfig);
        isFristRun = isFristRun();

    }

    /**
     * 判断是否第一次运行
     *
     * @return
     */
    public boolean isFristRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        boolean isshare = sharedPreferences.getBoolean("welcome", false);
        return !isshare;
    }

    /**
     * 设置数据库保存方法
     */
    public void onFirist() {
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("welcome", true);
        editor.commit();
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


    public DbManager getDbManager() {
        return dbManager;
    }

    public DBServices getDbServices() {
        return dbServices;
    }

    public void setDbServices(DBServices dbServices) {
        this.dbServices = dbServices;
    }

}
