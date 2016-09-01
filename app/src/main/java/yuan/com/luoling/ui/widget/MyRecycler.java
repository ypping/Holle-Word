package yuan.com.luoling.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyRecycler extends RecyclerView {

    /**
     * 记录当前第一个View
     */
    private View mCurrentView;

    public OnItemScrollChangeListener mItemScrollChangeListener;
    private int posi;

    public void setPosi(int position) {
        this.posi = position;
    }

    public void setOnItemScrollChangeListener(
            OnItemScrollChangeListener mItemScrollChangeListener) {
        this.mItemScrollChangeListener = mItemScrollChangeListener;
    }

    public interface OnItemScrollChangeListener {
        void onChange(View view, int position);
    }

    public MyRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(onScrollListener);

    }

    RecyclerView.OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }

        /**
         * 滚动时，判断当前第一个View是否发生变化，发生才回调
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            View newView = getChildAt(0);
            if (mItemScrollChangeListener != null) {
                if (newView != null && newView != mCurrentView) {
                    mCurrentView = newView;
                    mItemScrollChangeListener.onChange(mCurrentView,
                            getChildPosition(mCurrentView));
                }
            }

        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        View newView = getChildAt(posi);
        Log.i("myrecyler", posi + "");
        if (mItemScrollChangeListener != null) {
            if (newView != null && newView != mCurrentView) {
                mCurrentView = newView;
                mItemScrollChangeListener.onChange(mCurrentView,
                        getChildPosition(mCurrentView));
            }
        }
    }
}
