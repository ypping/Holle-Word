package yuan.com.luoling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import yuan.com.luoling.adapter.MyRecylerViewAdapter;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.inter.UpDataImageFile;
import yuan.com.luoling.inter.UpDataImageIm;
import yuan.com.luoling.ui.activity.ImageShow;
import yuan.com.luoling.ui.widget.MyRecycler;
import yuan.com.luoling.ui.widget.SpacesItemDecoration;
import yuan.com.luoling.utils.DensityUtil;

/**
 * Created by yuan-pc on 2016/05/23.
 */
public class MyActivity extends Activity {
    private final String TAG = "MyActivity";
    private MyRecycler recyclerView;
    private ListDate data = ListDate.getListData();
    private MyRecylerViewAdapter viewAdapter;
    private ViewPager viewPager;
    private Object tag = new Object();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        UpDataImageIm.getInstance().setUpDataImageFile(new UpDataImageFile() {
            @Override
            public void upDataImage() {
                viewAdapter.getData(data);
            }
        });
    }

    private void init() {
        recyclerView = (MyRecycler) findViewById(R.id.list_my_recylerView);
        viewPager = (ViewPager) findViewById(R.id.list_my_viewpager);
        viewPager.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(DensityUtil.dip2px(this, 10));
        recyclerView.addItemDecoration(decoration);
          Log.e(TAG, TAG + ":" + data.getImageFiles().size());
        viewAdapter = new MyRecylerViewAdapter(this, data, tag);
        /**
         * item的点击事件
         */
        viewAdapter.setOnItemClickListener(new MyRecylerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyActivity.this, ImageShow.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        viewAdapter.setOnItemShow(new MyRecylerViewAdapter.OnItemShow() {
            @Override
            public void onItemShow(View view, int position) {
            }
        });
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setOnItemScrollChangeListener(new MyRecycler.OnItemScrollChangeListener() {
            @Override
            public void onChange(View view, int position) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(MyActivity.this).resumeTag(tag);
                } else {
                    Picasso.with(MyActivity.this).pauseTag(tag);
                }
            }
        });
        /**
         * viewpager的点击监听
         */
       /* viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                recyclerView.setPosi(position);
                recyclerView.scrollToPosition(position);
                myPagerAdapter.notifyDataSetChanged();
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }


}
