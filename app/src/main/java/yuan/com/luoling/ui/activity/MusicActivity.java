package yuan.com.luoling.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.R;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.services.MusicServices;
import yuan.com.luoling.ui.widget.LRCView;
import yuan.com.luoling.utils.ActivityBar;
import yuan.com.luoling.utils.TimeUtils;

/**
 * 音乐二级目录
 * Created by YUAN on 2016/9/21.
 */

public class MusicActivity extends Activity {
    private final String TAG = "MusicActivity";
    private View munu, leftMusic, reihtMusic;
    private TextView title, startTime, endTime, textLrc;
    private FrameLayout frameLayout;
    private SeekBar seekBar;
    private CheckBox checkBox;
    private int position;
    private List<MusicFiles> musicFiles = ListDate.getListData().getMusicFiles();
    private LRCView lrcView;
    private MusicServices services = MyApplication.getApp().getMusicServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ActivityBar.setTranslucent(this);
        position = getIntent().getIntExtra("position", 0);
        initView();
    }


    /**
     * 绑定控件即监听
     */
    private void initView() {
        munu = findViewById(R.id.menu);
        leftMusic = findViewById(R.id.leftMusic);
        reihtMusic = findViewById(R.id.rightMusic);
        title = (TextView) findViewById(R.id.title);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);
        //frameLayout = (FrameLayout) findViewById(R.id.musicFrame);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        checkBox = (CheckBox) findViewById(R.id.stopMusic);
        lrcView = (LRCView) findViewById(R.id._musicLRC);
        textLrc = (TextView) findViewById(R.id.text_lrc);
        lrcView.bindSeekBar(seekBar);
        try {
            Log.e(TAG, TAG + "lrcView" + musicFiles.get(position).getLrcURL());
            lrcView.setLRCPath(musicFiles.get(position).getLrcURL());

            //
        } catch (IOException e) {
            e.printStackTrace();
        }
        lrcView.setNotLRCFile(new LRCView.NotLRCFile() {
            @Override
            public void onNotLrcFile() {
                textLrc.setVisibility(View.VISIBLE);
                lrcView.setVisibility(View.GONE);
            }

            @Override
            public void onHaveLrcFile() {
                textLrc.setVisibility(View.GONE);
                lrcView.setVisibility(View.VISIBLE);
            }
        });
        title.setText(musicFiles.get(position).getName());
        startTime.setText(String.valueOf(00.00));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String fileTime = "00.00";
        if (musicFiles.get(position).getTime() == 0) {

        } else {
            fileTime = String.valueOf(TimeUtils.milliseconds2String(musicFiles.get(position).getTime(), simpleDateFormat));
        }
        endTime.setText(fileTime);
        munu.setOnClickListener(onClickListener);
        leftMusic.setOnClickListener(onClickListener);
        reihtMusic.setOnClickListener(onClickListener);
        /**
         * 音乐播放按钮的监听
         */
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    services.setPlayMusic(position, MusicActivity.this);
                    lrcView.bindPlayBtnAndTimeText(seekBar, startTime, endTime);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu:

                    break;
                case R.id.leftMusic:
                    position = position - 1;
                    if (position >= 0) {
                        title.setText(musicFiles.get(position).getName());
                        services.leftMusic(position);
                        Log.e(TAG, TAG + "left==" + position);
                    } else {
                        position = 0;
                    }
                    break;
                case R.id.rightMusic:
                    position = position + 1;
                    if (position < musicFiles.size()) {
                        title.setText(musicFiles.get(position).getName());
                        services.rightMusic(position);
                        Log.e(TAG, TAG + "right==" + position);
                    } else {
                        position = musicFiles.size();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
