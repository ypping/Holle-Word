package yuan.com.luoling.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.bean.VideoFiles;
import yuan.com.luoling.db.DBCursorUtils;
import yuan.com.luoling.db.MyDbService;
import yuan.com.luoling.services.DBServices;
import yuan.com.luoling.ui.fragment.WelcomeFragment;
import yuan.com.luoling.utils.DensityUtil;
import yuan.com.luoling.utils.LuoLingComparator;

/**
 * Created by YUAN on 2016/9/9.
 */
public class WelcomeActivity extends FragmentActivity {
    private List<ImageFiles> imageFiles = new ArrayList<>();
    private final String TAG = "WecomeActivity";
    private DBServices dbServices = MyApplication.getApp().getDbServices();
    private MyDbService myDbService = MyDbService.getInterest();
    /**
     * fragment布局
     */
    private FrameLayout frameLayout;
    /**
     * fragment管理器
     */
    private FragmentManager manager = getSupportFragmentManager();
    /**
     * 引导页的fragment
     */
    private WelcomeFragment welcomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wecome);
        sequenceImageList();
        sequenceMusicList();
        sequenceVideoList();
        welcomeFragment = new WelcomeFragment();
        frameLayout = (FrameLayout) findViewById(R.id.wecome_Fragme);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.wecome_Fragme, welcomeFragment);
        transaction.commit();
        welcomeFragment.setContinueToGIF(upDataImageFile);
    }


    /**
     * 监听并加载动画的回调
     */
    WelcomeFragment.ContinueToGIF upDataImageFile = new WelcomeFragment.ContinueToGIF() {

        @Override
        public void onContinueToGIF() {
            for (int j = 0; j < 4; j++) {
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                welcomeFragment = new WelcomeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", j);
                welcomeFragment.setArguments(bundle);
                welcomeFragment.setContinueToGIF(upDataImageFile);
                fragmentTransaction.replace(R.id.wecome_Fragme, welcomeFragment);
                fragmentTransaction.commit();
            }
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    /**
     * 遍历图片的集合
     */
    private void sequenceImageList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("services", String.valueOf(MyApplication.getApp().getDbServices()));
                imageFiles = MyApplication.getApp().getDbServices().fistApp(imageFiles);
                Log.i("services", imageFiles.size() + "");
                try {
                    //将图片排序以图片大小进行排序
                    LuoLingComparator comparatorImage = new LuoLingComparator(ImageFiles.class);
                    Collections.sort(imageFiles, comparatorImage);
                    //倒叙处理
                    Collections.reverse(imageFiles);
                    ListDate.getListData().setImageFiles(imageFiles);
                    Log.e(TAG, TAG + "mdd" + imageFiles.size());
                    myDbService.addImageDB(imageFiles);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 从媒体数据库取音乐的集合
     */
    private void sequenceMusicList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (MusicFiles.class) {
                    Log.i("services", String.valueOf(MyApplication.getApp().getDbServices()));
                    List<MusicFiles> musicFiles = MyApplication.getApp().getDbServices().curcorMusic(WelcomeActivity.this);
                    Log.i("services", musicFiles.size() + "");
                    sequenceLRC(musicFiles);
                }
            }
        }).start();
    }

    /**
     * 遍历出lrc文件
     */
    private void sequenceLRC(final List<MusicFiles> musicFiles) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<File> fileList = new ArrayList<File>();
                fileList = DBCursorUtils.fillLRC(Environment.getExternalStorageDirectory().getPath(), fileList);
                Log.e(TAG, TAG + "fileList==" + fileList);
                for (MusicFiles music : musicFiles) {
                    DensityUtil.matchingLRC(music, fileList);
                }
                ListDate.getListData().setMusicFiles(musicFiles);
                Log.e(TAG, TAG + "mdd" + musicFiles.size());
                try {
                    myDbService.addMusicDB(musicFiles);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 添加视频到我的数据库
     */
    private void sequenceVideoList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("services", String.valueOf(MyApplication.getApp().getDbServices()));
                List<VideoFiles> videoFiles = MyApplication.getApp().getDbServices().curcorVideo(WelcomeActivity.this);
                Log.i("services", videoFiles.size() + "");
                try {
                    ListDate.getListData().setVideoFiles(videoFiles);
                    Log.e(TAG, TAG + "mdd" + videoFiles.size());
                    myDbService.addVideoDB(videoFiles);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
