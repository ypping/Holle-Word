package yuan.com.luoling.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import yuan.com.luoling.R;

/**
 * Created by YUAN on 2016/9/12.
 */
public class WelcomeFragment extends Fragment {
    private final String TAG = "WelcomeFragment";
    /**
     * 图片数组
     */
    private int[] pic = new int[3];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        for (int i = 0; i < pic.length; i++) {
            pic[i] = R.raw.c317d50506cee860b1284b28fd47101f;
        }
        try {
            final GifDrawable gifDrawable = new GifDrawable(getResources(), R.raw.c317d50506cee860b1284b28fd47101f);
            imageView.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
            gifDrawable.removeAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    gifDrawable.recycle();
                }
            });
            gifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                        continueToGIF.onContinueToGIF();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageView;
    }

    /**
     * 在一个动画播放完成后，继续播放下一个动画的回掉
     */
    private ContinueToGIF continueToGIF;

    /**
     * 接口定义
     */
    public interface ContinueToGIF {
        /**
         * 加载gif动画回调
         */
        void onContinueToGIF();
    }

    /**
     * 接口实现方法
     *
     * @param continueToGIF
     */
    public void setContinueToGIF(ContinueToGIF continueToGIF) {
        this.continueToGIF = continueToGIF;
    }
}
