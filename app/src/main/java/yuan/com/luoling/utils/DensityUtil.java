package yuan.com.luoling.utils;

import android.content.Context;


@SuppressWarnings("deprecation")
public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 绘制九宫格图片
     *
     * @param mContext
     * @param imageLayout
     * @param imgs
     *//*
    public static void sudoku(Context mContext, AbsoluteLayout imageLayout, BmobFile[] imgs, OnClickListener l) {
		if (imgs != null) {
			int w = imageLayout.getWidth();
			LayoutParams params;

			int i = 0;
			ImageView v;
			if (imgs.length == 1) {
				params = new LayoutParams(-2, -2, dip2px(mContext, 10), 0);
				v = new ImageView(mContext);
				imageLayout.addView(v, params);
				v.setBackgroundColor(Color.LTGRAY);
				v.setId(1000 + i);
				v.setOnClickListener(l);
				imgs[i].loadImage(mContext, v, w / 2, w / 2);

			} else if (imgs.length == 2) {
				params = new LayoutParams(-2, -2, 0, 0);
				v = new ImageView(mContext);
				v.setBackgroundColor(Color.LTGRAY);
				v.setId(1000 + i);
				v.setOnClickListener(l);
				imageLayout.addView(v, params);
				imgs[i].loadImageThumbnail(mContext, v, w / 2 - dip2px(mContext, 35), w / 2 - dip2px(mContext, 35));
				i++;
				params = new LayoutParams(w / 2 - dip2px(mContext, 35), w / 2 - dip2px(mContext, 35),
						(w / 2) - dip2px(mContext, 30), 0);
				v = new ImageView(mContext);
				v.setBackgroundColor(Color.LTGRAY);
				v.setId(1000 + i);
				v.setOnClickListener(l);
				imageLayout.addView(v, params);
				imgs[i].loadImageThumbnail(mContext, v, w / 2 - dip2px(mContext, 35), w / 2 - dip2px(mContext, 35));
			} else if (imgs.length == 4) {

				for (BmobFile p : imgs) {

					int x = ((i % 2) * w / 2) - dip2px(mContext, 30);
					int y = ((i / 2) * w / 2) - dip2px(mContext, 30);
					if (y < 0)
						y = 0;
					if (x < 0)
						x = 0;
					params = new LayoutParams(-2, -2, x, y);
					v = new ImageView(mContext);
					v.setBackgroundColor(Color.LTGRAY);					 
					v.setId(1000 + i);
					v.setOnClickListener(l);
					imageLayout.addView(v, params);
					p.loadImage(mContext, v,w / 2 - dip2px(mContext, 35), w / 2 - dip2px(mContext, 35));
					i++;
				}
			} else {
				for (BmobFile p : imgs) {
					if (i == 9) {
						break;// 只显示9张图片！
					}
					params = new LayoutParams(-2, -2, (i % 3) * w / 3, (i / 3) * w / 3);
					v = new ImageView(mContext);
					v.setBackgroundColor(Color.LTGRAY);
					v.setId(1000 + i);
					v.setOnClickListener(l);
					imageLayout.addView(v, params);
					p.loadImage(mContext, v, w / 3 - dip2px(mContext, 5), w / 3 - dip2px(mContext, 5));
					i++;
				}
			}
		}
	}*/
}
