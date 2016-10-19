package yuan.com.luoling.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yuan.com.luoling.ActivityManagement;
import yuan.com.luoling.MyApplication;
import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.bean.VideoFiles;
import yuan.com.luoling.db.DBCursorUtils;
import yuan.com.luoling.db.MyDbService;
import yuan.com.luoling.ui.fragment.ImageFragment;
import yuan.com.luoling.ui.fragment.MusicFragment;
import yuan.com.luoling.ui.fragment.VideoFragment;
import yuan.com.luoling.utils.ActivityBar;
import yuan.com.luoling.utils.DensityUtil;
import yuan.com.luoling.utils.LuoLingComparator;
import yuan.com.luoling.widget.LoadingDialog;
import yuan.com.luoling.widget.MenuPopupWindows;

/**
 * Created by YUAN on 2016/9/6.
 */
public class HomeActivity extends FragmentActivity implements MusicFragment.OnListFragmentInteractionListener {
    private final String TAG = "HomeActivity";
    private RadioGroup radioGroup;
    private RadioButton[] radioButtons = new RadioButton[3];
    private Fragment[] fragments = new Fragment[3];
    private MusicFragment musicFragment;
    private VideoFragment videoFragment;
    private ImageFragment imageFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private ImageView rightImage;
    private String[] titles = new String[]{"小娄娄的音乐", "小娄娄的图片", "小娄娄的视频"};
    private TextView title;
    private MenuPopupWindows menuPopupWindows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBar.setStatusBarColor(this, R.color.promotionbar);
        //activity的集合添加当前的activity
        ActivityManagement.getInstance().sendActivity.createActivity(this);
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {

        radioGroup = (RadioGroup) findViewById(R.id.main_radio);
        radioButtons[0] = (RadioButton) findViewById(R.id.main_music);
        radioButtons[1] = (RadioButton) findViewById(R.id.main_image);
        radioButtons[2] = (RadioButton) findViewById(R.id.main_video);
        rightImage = (ImageView) findViewById(R.id.rightImage);
        rightImage.setImageResource(R.mipmap.menu);
        rightImage.setPadding(DensityUtil.dip2px(this, 16), 0, DensityUtil.dip2px(this, 16), DensityUtil.dip2px(this, 5));
        title = (TextView) findViewById(R.id.title);
        title.setText(titles[0]);
        rightImage.setVisibility(View.VISIBLE);
        imageFragment = new ImageFragment();
        musicFragment = new MusicFragment();
        videoFragment = new VideoFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragments[0] = musicFragment;
        fragments[1] = imageFragment;
        fragments[2] = videoFragment;
        fragmentTransaction.add(R.id.main_fragmen, fragments[0]);
        fragmentTransaction.add(R.id.main_fragmen, fragments[1]);
        fragmentTransaction.add(R.id.main_fragmen, fragments[2]);
        fragmentTransaction.show(fragments[0]);
        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.hide(fragments[2]);
        //fragment之间的动画效果
        fragmentTransaction.setCustomAnimations(R.anim.close_exit, R.anim.close_enter);
        fragmentTransaction.commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showAndHide();
            }
        });

        rightImage.setOnClickListener(onClickListener);

    }

    private void showAndHide() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                Log.e(TAG, TAG + radioButtons[i].isChecked());
                fragmentTransaction.show(fragments[i]);
                title.setText(titles[i]);
            } else {
                Log.e(TAG, TAG + radioButtons[i].isChecked());
                fragmentTransaction.hide(fragments[i]);
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(MusicFiles musicFiles) {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, TAG + v.getId());
            switch (v.getId()) {
                case R.id.rightImage:
                    showPopup(v);
                    break;
                case R.id.menu_music:
                    menuPopupWindows.dismiss();
                    ScanningMath(0);
                    break;
                case R.id.menu_video:
                    menuPopupWindows.dismiss();
                    ScanningMath(2);
                    break;
                case R.id.menu_image:
                    menuPopupWindows.dismiss();
                    ScanningMath(1);
                    break;
            }
        }
    };

    /**
     * 显示popupMenu的方法
     */
    private void showPopup(View view) {

        menuPopupWindows = new MenuPopupWindows(this, onClickListener, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuPopupWindows.showPopup(view);
    }

    LoadingDialog loadingDialog;

    /**
     * 同步的一个方法，希望里面的程序是顺序执行的
     *
     * @param position
     */
    private synchronized void ScanningMath(int position) {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        Log.d(TAG, TAG + ":::" + loadingDialog.isShowing());
        switch (position) {
            case 0:
                traverseMusicList();
                break;
            case 1:
                onImage();
                break;
            case 2:
                sequenceVideoList();
                break;
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadingDialog.dismiss();
            refreshData.onRefreshData();
        }
    };

    private void onImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ImageFiles> imageFiles = (MyApplication.getApp().getDbServices().fistApp(new ArrayList<ImageFiles>()));
                    LuoLingComparator comparatorImage = new LuoLingComparator(ImageFiles.class);
                    Collections.sort(imageFiles, comparatorImage);
                    //倒叙处理
                    Collections.reverse(imageFiles);
                    ListDate.getListData().setImageFiles(imageFiles);
                    Log.e(TAG, TAG + "Image" + imageFiles.size());

                    MyDbService.getInterest().updateImageDB(imageFiles);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                refreshData.onRefreshData();
                handler.sendEmptyMessage(1);
            }
        }).start();

    }

    /**
     * 遍历音乐文件
     */
    private void traverseMusicList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (MusicFiles.class) {
                    Log.i("services", String.valueOf(MyApplication.getApp().getDbServices()));
                    List<MusicFiles> musicFiles = new ArrayList<MusicFiles>();
                    musicFiles = MyApplication.getApp().getDbServices().findMusic(musicFiles);
                    //     Log.i("services", musicFiles.size() + "");
                    //将图片排序以图片大小进行排序
                    LuoLingComparator comparatorImage = new LuoLingComparator(MusicFiles.class);
                    Collections.sort(musicFiles, comparatorImage);
                    //倒叙处理
                    Collections.reverse(musicFiles);
                    ListDate.getListData().setMusicFiles(musicFiles);
                    Log.e(TAG, TAG + "music" + musicFiles.size());
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
                Log.e(TAG, TAG + "musicLrc" + musicFiles.size());
                try {
                    MyDbService.getInterest().updateMusicDB(musicFiles);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
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
                List<VideoFiles> videoFiles = MyApplication.getApp().getDbServices().curcorVideo(HomeActivity.this);
                Log.i("services", videoFiles.size() + "");
                try {
                    ListDate.getListData().setVideoFiles(videoFiles);
                    Log.e(TAG, TAG + "video" + videoFiles.size());
                    MyDbService.getInterest().addVideoDB(videoFiles);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 传入一个activity，实现activity集合的删除
         */
        ActivityManagement.getInstance().sendActivity.destroyActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    private RefreshData refreshData;

    public void setRefreshData(RefreshData refreshData) {
        this.refreshData = refreshData;
    }

    public interface RefreshData {
        void onRefreshData();
    }
}
