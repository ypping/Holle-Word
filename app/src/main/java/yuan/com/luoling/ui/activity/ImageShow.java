package yuan.com.luoling.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ContentView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFile;
import yuan.com.luoling.bean.ListDate;

/**
 * 一个全屏显示图片的activity
 * Created by yuan-pc on 2016/06/01.
 */
@ContentView(value = R.layout.image_show)
public class ImageShow extends Activity {
    //从myActivity获得的数据加载到本页面形成一个可以滑动的页面
    private List<?> list;
    private MyPagerAdapter myPagerAdapter;
    private ListDate data;
    //绑定页面与资源id

    private ViewPager viewPager;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show);
        viewPager = (ViewPager) findViewById(R.id.image_show_image);
        ObjectAnimator animator = ObjectAnimator.ofFloat(viewPager, "alpha", 0.0f, 0.0f, 1.0F);
        data = ListDate.getListData();
        if (data.getImageFiles() != null) {
            this.list = data.getImageFiles();
        }

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(position);
        animator.start();
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
    }

    public class MyPagerAdapter extends PagerAdapter {

        public MyPagerAdapter() {

        }

        @Override
        public int getCount() {
            if (data.getImageFiles() != null) {
                return data.getImageFiles().size();
            } else {
                data.setImageFiles(new ArrayList<ImageFile>());
                return data.getImageFiles().size();
            }
        }

        public void setData() {
            if (data.getImageFiles() != null) {
                list = data.getImageFiles();
            } else {
                data.setImageFiles(new ArrayList<ImageFile>());
                list = data.getImageFiles();
            }
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
            ImageView v = new ImageView(ImageShow.this);

            v.setId(position);
            if (data.getImageFiles().get(position).getName().toLowerCase().endsWith(".gif")) {
                Glide.with(ImageShow.this).load(data.getImageFiles().get(position).getPath()).asGif().placeholder(R.mipmap.summer_icon).into(v);
            } else {
                Picasso.with(getApplication()).load(new File(data.getImageFiles().get(position).getPath())).placeholder(R.mipmap.summer_icon).into(v);
            }

            container.addView(v, -1, -1);
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
