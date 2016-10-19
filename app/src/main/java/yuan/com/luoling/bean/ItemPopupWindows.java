package yuan.com.luoling.bean;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by YUAN on 2016/10/14.
 */

public class ItemPopupWindows {
    private String mTitle;
    private Drawable mDrawable;

    public ItemPopupWindows(String mTitle, Drawable mDrawable) {
        this.mTitle = mTitle;
        this.mDrawable = mDrawable;
    }

    public ItemPopupWindows(Context context, int mTitle, int mDrawable) {
        this.mTitle = context.getResources().getString(mTitle);
        this.mDrawable = context.getResources().getDrawable(mDrawable);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ItemPopupWindows(Context context, String mTitle, int mDrawable) {
        this.mTitle = mTitle;
        this.mDrawable = context.getDrawable(mDrawable);
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }
}
