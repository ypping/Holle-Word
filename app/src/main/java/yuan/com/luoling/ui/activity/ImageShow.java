package yuan.com.luoling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.ui.widget.ImagePageTransforser;
import yuan.com.luoling.utils.ActivityBar;

/**
 * 一个全屏显示图片的activity
 * Created by yuan-pc on 2016/06/01.
 */
@ContentView(value = R.layout.image_show)
public class ImageShow extends Activity {
    private final String TAG = "ImageShow";
    //从myActivity获得的数据加载到本页面形成一个可以滑动的页面
    private MyPagerAdapter myPagerAdapter;
    private ListDate data;
    //绑定页面与资源id
    private ViewPager viewPager;
    int position;
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBar.setStatusBarColor(this, R.color.colorAlipe);
        setContentView(R.layout.image_show);

        viewPager = (ViewPager) findViewById(R.id.image_show_image);
        back = findViewById(R.id.back);
        data = ListDate.getListData();
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.setPageTransformer(true, new ImagePageTransforser());
        initView();
    }

    private void initView() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                myPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class MyPagerAdapter extends PagerAdapter {

        public MyPagerAdapter() {

        }

        @Override
        public int getCount() {
            if (data.getImageFiles() != null) {
                return data.getImageFiles().size();
            } else {
                data.setImageFiles(new ArrayList<ImageFiles>());
                return data.getImageFiles().size();
            }
        }

        public void setData() {
            notifyDataSetChanged();
        }

        /**
         * 初始item的位置
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView v = new ImageView(ImageShow.this);
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            v.setId(position);
            if (data.getImageFiles().get(position).getName().toLowerCase().endsWith(".gif")) {
                Glide.with(ImageShow.this).load(data.getImageFiles().get(position).getPath())
                        .asGif().into(v);
            } else {

                Glide.with(ImageShow.this).load(data.getImageFiles().get(position).getPath()).placeholder(R.mipmap.moren)
                        .skipMemoryCache(false).into(v);
            }

            container.addView(v);
            return v;
        }

        /**
         * 判断当前view是否再用
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 销毁废弃的item
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
        }
    }

}
