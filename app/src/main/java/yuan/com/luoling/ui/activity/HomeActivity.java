package yuan.com.luoling.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.ui.fragment.ImageFragment;
import yuan.com.luoling.ui.fragment.MusicFragment;
import yuan.com.luoling.ui.fragment.VideoFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
        fragmentTransaction.commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showAndHide();
            }
        });

    }

    private void showAndHide() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                Log.e(TAG, TAG + radioButtons[i].isChecked());
                fragmentTransaction.show(fragments[i]);
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
}
