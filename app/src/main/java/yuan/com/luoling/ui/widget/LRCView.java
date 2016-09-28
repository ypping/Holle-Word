package yuan.com.luoling.ui.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.io.IOException;
import java.util.Set;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.bean.LrcData;
import yuan.com.luoling.services.DBServices;

/**
 * Created by YUAN on 2016/9/27.
 */

public class LRCView extends View {
    private final String TAG = "LRCView";
    private LrcData mLrc;
    private Integer currentTime;
    private Integer currentIndex;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBar;
    private TextView currentTimeText;
    private TextView musicTimeText;
    private MediaPlayer.OnCompletionListener playComletion;
    private boolean isChanging;
    private float mX;
    private float mY;
    private float mMiddleY;
    /**
     * 动画偏移过后的middleY;
     */
    private float offedMiddleY;
    /**
     * 移动过后的
     */
    private float moveOffY;
    private float space;
    Paint p1 = new Paint();
    Paint p2 = new Paint();

    public LRCView(Context context) {
        super(context);
        p1.setTextSize(dip2px(13));
        p2.setTextSize(dip2px(16));
        p1.setColor(Color.WHITE);
        p2.setColor(Color.YELLOW);
        space = p1.getTextSize() * 2.3f;
        MyApplication.getApp().getDbServices().setMusicListener(musicListener);
    }

    public LRCView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p1.setTextSize(dip2px(13));
        p2.setTextSize(dip2px(16));
        p1.setColor(Color.WHITE);
        p2.setColor(Color.YELLOW);
        space = p1.getTextSize() * 2.3f;
        MyApplication.getApp().getDbServices().setMusicListener(musicListener);
    }

    public LRCView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p1.setTextSize(dip2px(13));
        p2.setTextSize(dip2px(16));
        p1.setColor(Color.WHITE);
        p2.setColor(Color.YELLOW);
        space = p1.getTextSize() * 2.3f;
        MyApplication.getApp().getDbServices().setMusicListener(musicListener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LRCView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        p1.setTextSize(dip2px(13));
        p2.setTextSize(dip2px(16));
        p1.setColor(Color.WHITE);
        p2.setColor(Color.YELLOW);
        space = p1.getTextSize() * 2.3f;
        MyApplication.getApp().getDbServices().setMusicListener(musicListener);
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public void setmPlayer(MediaPlayer mPlayer) {
        this.mPlayer = mPlayer;
    }

    public float getTextSize() {
        return p1.getTextSize();
    }

    public void setTextSize(float textSize) {
        p1.setTextSize(textSize);

    }

    public float getCurrentTextSize() {
        return p2.getTextSize();
    }

    public void setCurrentTextSize(float currentTextSize) {
        p2.setTextSize(currentTextSize);
    }

    public int getTextColor() {
        return p1.getColor();
    }

    public void setTextColor(int textColor) {
        p1.setColor(textColor);
    }

    public int getCurrentLrcTextColor() {
        return p2.getColor();
    }

    public void setCurrentLrcTextColor(int currentTextColor) {
        p2.setColor(currentTextColor);
    }

    public float getSpace() {
        return space - p1.getTextSize();
    }

    public void setLineSpace(float space) {
        this.space = p1.getTextSize() * space;
    }

    public boolean isChanging() {
        return isChanging;
    }

    public void setChanging(boolean changing) {
        isChanging = changing;
    }

    public void setLRC(LrcData lrc) {
        mLrc = lrc;
        Set<Integer> key = mLrc.getLyrics().keySet();

        for (Integer k : key) {
            currentTime = k;
            break;
        }
        currentIndex = 0;
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (super.getVisibility() == View.GONE) {
            return;
        }
        super.onDraw(canvas);
        if (mLrc == null)
            return;
        Set<Integer> key = mLrc.getLyrics().keySet();
        float _mY = offedMiddleY + moveOffY;
        drawOneLrc(currentTime, canvas, p2, _mY);
        int i = 0;
        int alpha = 0;
        float y;
        for (Integer k : key) {
            if (i < currentIndex) {
                y = _mY - (currentIndex - i) * space;
                if (y > space) {
                    if (y > 1.5f * space) {
                        p1.setAlpha(255);
                    } else {
                        p1.setAlpha(155);
                    }
                    drawOneLrc(k, canvas, p1, y);
                }

            } else if (i > currentIndex) {
                y = _mY + (i - currentIndex) * space;

                if (y <= mY) {
                    float bt = mY - y;
                    if (bt > 2.5f * space) {
                        alpha = 255;
                    } else if (bt > 1.2f * space) {
                        alpha = 160;
                    } else {
                        alpha = 68;
                    }
                    p1.setAlpha(alpha);
                    drawOneLrc(k, canvas, p1, y);
                }
            }
            i++;
        }
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void setPlayMusic(String path)
            throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        intiMediaPlay();
        mPlayer.setDataSource(path);
        mPlayer.prepare();
        intiOtherView();
    }

    public void setPlayMusic(Uri uri)
            throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        intiMediaPlay();
        mPlayer.setDataSource(getContext(), uri);
        mPlayer.prepare();
        intiOtherView();
    }

    private void intiMediaPlay() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(playComletion);

        } else
            mPlayer.stop();
        mPlayer.reset();
    }

    private void intiOtherView() {

        if (musicTimeText != null)
            musicTimeText.setText(parseTimeToString(mPlayer.getDuration()));
        if (mSeekBar != null) {
            mSeekBar.setProgress(mPlayer.getCurrentPosition());
        }
        if (currentTimeText != null) {
            currentTimeText.setText("00:00");
        }
    }

    public void setLRCPath(String path) throws IOException {

        LrcData lrc = LrcData.loadLRCFile(path);
        setLRC(lrc);
    }

    private String getOneLrc(Integer time) {
        String c = mLrc.getLyrics().get(time);
        if (c == null) {
            return "";
        } else {
            return c;
        }
    }

    private void drawOneLrc(Integer time, Canvas canvas, Paint p, float y) {
        String c = getOneLrc(time);
        float offx = p.measureText(c) / 2f;
        canvas.drawText(c, mX - offx, y, p);
    }

    ValueAnimator animation;
    private int nextTime = -1;
    private float animOff;
    private float lastOff;
    private boolean isCancelAnim;
    private ValueAnimator.AnimatorUpdateListener ainimationListener = new ValueAnimator.AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animOff = (Float) animation.getAnimatedValue();
            offedMiddleY = mMiddleY + animOff * (1 + lastOff) - lastOff * space;
            invalidate();
        }
    };
    private Animator.AnimatorListener aListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator anim) {
            animation = (ValueAnimator) anim;
            if (!isCancelAnim) {
                lastOff = 0;
            } else {
                isCancelAnim = false;
            }
        }

        @Override
        public void onAnimationCancel(Animator anima) {

        }
    };

    public void setCurrentTime(int time) {
        if (mSeekBar != null && !isChanging)
            mSeekBar.setProgress(time);
        if (currentTimeText != null) {
            String t = parseTimeToString(time);
            if (!currentTimeText.getText().toString().equals(t)) {
                currentTimeText.setText(t);
            }
        }
        if (mLrc == null)
            return;
        Set<Integer> key = mLrc.getLyrics().keySet();
        int ct = 0;
        int i = 0;
        int md = 0;
        for (int k : key) {
            if (k <= time) {
                ct = k;
                currentIndex = i;
            } else {
                nextTime = k;
                md = k - time;
                break;
            }
            i++;
        }
        if (currentTime != ct) {
            currentTime = ct;
            invalidate();
            if (mPlayer.isPlaying())
                animotion(md);
        }
    }

    Runnable mRun = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(mPlayer.getCurrentPosition());
        }
    };
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (mPlayer.isPlaying()) {
                setCurrentTime(msg.what);
                mHandler.postDelayed(mRun, 10);
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener l = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mPlayer == null)
                return;
            mPlayer.seekTo(seekBar.getProgress());
            if (!mPlayer.isPlaying()) {
                setCurrentTime(seekBar.getProgress());
            }
            isChanging = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isChanging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
    };

    public void play() {
        if (animation != null && nextTime - mPlayer.getCurrentPosition() > 0) {
            animotion(nextTime - mPlayer.getCurrentPosition());
        }
        mPlayer.start();
        if (mSeekBar != null)
            mSeekBar.setMax(mPlayer.getDuration());
        mHandler.postDelayed(mRun, 10);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void goOn(String path) throws IOException {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        mPlayer.setDataSource(path);
        mPlayer.prepare();

        mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.e(TAG, TAG + ">>" + mp.isPlaying());
            }
        });
        mPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {
                Log.e(TAG, TAG + ">>" + mp.isPlaying() + "text" + text.toString());
            }
        });
    }

    public void pause() {
        mPlayer.pause();
        if (animation != null) {
            animation.cancel();
            isCancelAnim = true;
            lastOff = animOff / space;
        }
    }

    public void stop() {
        mPlayer.stop();
        if (animation != null) {
            animation.cancel();
        }
    }

    public void bindSeekBar(SeekBar seekBar) {
        mSeekBar = seekBar;
        mSeekBar.setOnSeekBarChangeListener(l);
    }

    private void animotion(int duration) {
        if (animation == null)
            animation = ValueAnimator.ofFloat(0, -space);

        if (animation.isRunning()) {
            animation.cancel();
        }
        animation.setDuration(duration);
        animation.addUpdateListener(ainimationListener);
        animation.addListener(aListener);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();

    }

    ;

    /**
     * 须在设置完MediaPlay后执行此方法
     *
     * @param play
     * @param currentT
     * @param lengthT
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void bindPlayBtnAndTimeText(View play, TextView currentT, TextView lengthT) {
        musicTimeText = lengthT;
        if (musicTimeText != null && mPlayer != null) {
            musicTimeText.setText(parseTimeToString(mPlayer.getDuration()));
        }
        currentTimeText = currentT;
        if (currentTimeText != null && mPlayer != null) {
            currentTimeText.setText(parseTimeToString(mPlayer.getCurrentPosition()));
        }
    }

    @SuppressLint("DefaultLocale")
    private String parseTimeToString(int time) {
        int s = time / 1000;
        int m = s / 60;
        int ss = s % 60;
        String str = String.format("%02d:%02d", m, ss);
        return str;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mX = w * 0.5f;
        mY = h;
        mMiddleY = h * 0.4f;
        offedMiddleY = mMiddleY;

    }

    /**
     * 用户是否执行了翻页歌词
     */
    private boolean isDoMove;

    /**
     * 用户按下时的xy坐标
     */
    private float onDownY;

    private float offY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float off = event.getY() - onDownY;
            moveOffY = offY + off;
            invalidate();
            if (Math.abs(off) > space * 2) {
                isDoMove = true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            offY = moveOffY;
            onDownY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isDoMove) {
                moveOffY = 0;
            } else {
                isDoMove = false;
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isDoMove) {
                            moveOffY = 0;
                        }
                    }
                }.start();
            }
        }
        super.onTouchEvent(event);
        return true;
    }

    public int dip2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param listener
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        playComletion = listener;
        if (mPlayer != null) {
            mPlayer.setOnCompletionListener(playComletion);
            Log.i("music", "set listener");
        }
    }

    DBServices.MusicListener musicListener = new DBServices.MusicListener() {

        @Override
        public void playMusic(MediaPlayer mediaPlayer, String path) {
            mPlayer = mediaPlayer;
            try {
                setPlayMusic(path);
                play();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pauseMusic() {
            pause();
        }

        @Override
        public void stopMusic() {
            stop();
        }

        @Override
        public void goOnMusic() {
            play();
        }

        @Override
        public void runDirection(String path, int playPosition) {

        }

    };
}
