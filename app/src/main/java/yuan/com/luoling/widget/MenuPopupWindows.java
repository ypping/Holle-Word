package yuan.com.luoling.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import yuan.com.luoling.R;
import yuan.com.luoling.utils.ScreenUtils;

/**
 * Created by YUAN on 2016/10/18.
 */

public class MenuPopupWindows extends PopupWindow {
    private View mView;
    private View layoutMusic, layoutImage, layoutVideo;
    private Activity activity;
    /**
     * 坐标的位置
     */
    private final int[] mLocation = new int[2];

    public MenuPopupWindows(Activity activity, View.OnClickListener onClickListener, int width, int height) {
        super(activity);
        this.activity = activity;
        //窗口布局
        mView = LayoutInflater.from(activity).inflate(R.layout.menu_popup_windows, null);
        //查找布局属性
        layoutMusic = mView.findViewById(R.id.menu_music);
        layoutImage = mView.findViewById(R.id.menu_image);
        layoutVideo = mView.findViewById(R.id.menu_video);
        //为每个item设置点击事件
        if (onClickListener != null) {
            layoutMusic.setOnClickListener(onClickListener);
            layoutVideo.setOnClickListener(onClickListener);
            layoutImage.setOnClickListener(onClickListener);
        }
        //设置布局
        setContentView(mView);
        //设置宽度
        setWidth(width);
        //设置高度
        setHeight(height);
        //设置动画效果
        setAnimationStyle(R.style.AnimTools);
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void showPopup(View view) {
        view.getLocationOnScreen(mLocation);
        getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    dismiss();
                }
            }
        });

        //设置默认获取焦点
        setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        showAtLocation(view, Gravity.NO_GRAVITY, ScreenUtils.getScreenWidth(activity) - (view.getWidth()/2), mLocation[1] + view.getHeight());
        //如果窗口存在，则更新
        update();
    }
}
