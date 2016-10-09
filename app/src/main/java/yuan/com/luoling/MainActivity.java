package yuan.com.luoling;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.bean.VideoFiles;
import yuan.com.luoling.services.DBServices;
import yuan.com.luoling.ui.activity.HomeActivity;
import yuan.com.luoling.ui.activity.WelcomeActivity;
import yuan.com.luoling.utils.ActivityBar;
import yuan.com.luoling.utils.IntentLink;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private final int INTERNET_WIFI = 1;
    private final int INTERNET_CMWAP = 2;
    private final int INTERNET_CMNET = 3;
    @ViewInject(value = R.id.main_image2)
    private ImageView image2;
    String uri = "http://b214.photo.store.qq.com/psb?/89a3bb81-709c-4251-9f09-b09ed0d423e4/e9tmbvuZDTuLk*W9uqtDS2vMlYCr*ADo24i9obd5zIk!/b/YcWsoX..IgAAYkCTm3.AGwAA&bo=ngL2AQAAAAABA08!&rf=viewer_4";
    private DBServices dbServices;
    private ListDate date = ListDate.getListData();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        ShareSDK.initSDK(this);
        ActivityBar.setTranslucent(this);

        if (!IntentLink.isInternet(this)) {
            Toast.makeText(this, "请打开网络", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, DBServices.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        initWebView();
        int intenttype = IntentLink.getInternetType(this);
        Log.i("intentType", "intent:" + intenttype);
        setImage();
    }

    private void setData() {
        Log.i("intentType", "isFristrun" + MyApplication.getApp().isFristRun());
        if (MyApplication.getApp().isFristRun()) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            MyApplication.getApp().onFirist();
            finish();
        } else {
            getImageDB();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    };

    /**
     * 事件的绑定
     */
    private void initWebView() {

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        /*textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                MyApplication.getApp().onFirist();
              *//*  ShowShareSDK showShareSDK = new ShowShareSDK(MainActivity.this);
                showShareSDK.ShowShare("QQ中病毒了", "https://www.baidu.com/", "微信中病毒了", uri
                        , "此人QQ中病毒了，请勿相信发送内容", uri);*//*
            }
        });*/
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);

        animationSet.addAnimation(alphaAnimation);
        Animation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animation.setZAdjustment(2);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(MainActivity.this).load(R.mipmap.gif7).into(image2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animation.setRepeatCount(1);
            }
        });
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setData();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image2.setAnimation(animationSet);

    }

    private void login() {
       /* Platform qqLogin = ShareSDK.getPlatform(QQ.NAME);
        qqLogin*/
        Platform wechat = ShareSDK.getPlatform(MainActivity.this, QQ.NAME);

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e(TAG, TAG + "complete==" + platform.getName() + "::::" + i + "::::" + hashMap.get("name"));
                //   public void onComplete (Platform platform,int action, HashMap<String, Object > res){
                //用户资源都保存到res
                //通过打印res数据看看有哪些数据是你想要的
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    platDB.getToken();
                    platDB.getUserGender();
                    platDB.getUserIcon();
                    platDB.getUserId();
                    platDB.getUserName();
                    Log.e(TAG, TAG + "complete==" + platDB.getToken() + "::::" + platDB.getUserId() +
                            "::::" + platDB.getUserName());
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                platform.removeAccount(true);
                Log.e(TAG, TAG + "error==" + platform.getName() + "::::" + i + "::::" + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                platform.removeAccount(true);
                Log.e(TAG, TAG + "cancel==" + platform.toString() + "::::" + i);
            }
        });
        wechat.showUser(null);
        wechat.removeAccount(true);
    }

    private void setImage() {
        Glide.with(this).load(R.mipmap.luoling_start).into(image2);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("services", ((DBServices.ImageBinder) service).getServices().getPackageName());
            dbServices = (DBServices) ((DBServices.ImageBinder) service).getServices();
            MyApplication.getApp().setDbServices(dbServices);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dbServices = null;
            MyApplication.getApp().setDbServices(dbServices);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(serviceConnection);
    }

    private List<MusicFiles> musicFiles;

    /**
     * 获得图片数据库
     */
    private void getImageDB() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (ImageFiles.class) {
                    List<ImageFiles> files = new ArrayList<ImageFiles>();
                    try {
                        if (MyApplication.getApp().getDbManager().findAll(ImageFiles.class) != null)
                            files.addAll(MyApplication.getApp().getDbManager().findAll(ImageFiles.class));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    date.setImageFiles(files);
                }
                synchronized (MusicFiles.class) {
                    musicFiles = new ArrayList<MusicFiles>();
                    try {
                        if (MyApplication.getApp().getDbManager().findAll(MusicFiles.class) != null)
                            musicFiles.addAll(MyApplication.getApp().getDbManager().findAll(MusicFiles.class));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    date.setMusicFiles(musicFiles);
                }
                synchronized (VideoFiles.class) {
                    List<VideoFiles> videoFiles = new ArrayList<VideoFiles>();
                    try {
                        if (MyApplication.getApp().getDbManager().findAll(VideoFiles.class) != null)
                            videoFiles.addAll(MyApplication.getApp().getDbManager().findAll(VideoFiles.class));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    date.setVideoFiles(videoFiles);
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
