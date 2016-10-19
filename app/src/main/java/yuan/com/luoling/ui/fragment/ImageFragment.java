package yuan.com.luoling.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.db.MyDbService;
import yuan.com.luoling.inter.UpDataImageFile;
import yuan.com.luoling.inter.UpDataImageIm;
import yuan.com.luoling.ui.activity.HomeActivity;
import yuan.com.luoling.ui.activity.ImageShow;
import yuan.com.luoling.ui.adapter.ImageRecylerViewAdapter;
import yuan.com.luoling.ui.widget.ConfirmDialog;
import yuan.com.luoling.ui.widget.SpacesItemDecoration;
import yuan.com.luoling.utils.DensityUtil;
import yuan.com.luoling.widget.pulltorefres.library.extras.recyclerview.PullToRefreshRecyclerView;

/**
 * Created by YUAN on 2016/9/8.
 */
public class ImageFragment extends Fragment {
    private final String TAG = "ImageFragment";
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private ListDate data = ListDate.getListData();
    private ImageRecylerViewAdapter viewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setTheme(R.style.Theme_AppStartLoad);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_list, null);
        init(view);
        UpDataImageIm.getInstance().setUpDataImageFile(new UpDataImageFile() {
            @Override
            public void upDataImage() {
                viewAdapter.getData(data.getImageFiles());
            }
        });
        ((HomeActivity) getActivity()).setRefreshData(new HomeActivity.RefreshData() {
            @Override
            public void onRefreshData() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getActivity()).clearDiskCache();
                    }
                }).start();
                Glide.get(getActivity()).clearMemory();
                viewAdapter.getData(data.getImageFiles());
            }
        });
        return view;
    }

    private void init(LinearLayout view) {
        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.list_my_recylerView);
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(DensityUtil.dip2px(getActivity(), 5));
        recyclerView.addItemDecoration(decoration);
        TextView textView = new TextView(getActivity());
        Log.e(TAG, TAG + "size" + ListDate.getListData().getVideoFiles().size());
        viewAdapter = new ImageRecylerViewAdapter(getActivity(), data.getImageFiles());
        recyclerView.setAdapter(viewAdapter);
        textView.setText("没有图片，请扫描添加图片后扫描");
        textView.setTextSize(DensityUtil.dip2px(getActivity(), 16));
        textView.setTextColor(Color.WHITE);
        if (data.getImageFiles().size() == 0) {
            textView.setVisibility(View.VISIBLE);
            pullToRefreshRecyclerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
        }
        view.addView(textView);
        /**
         * item的点击事件
         */
        viewAdapter.setOnItemClickListener(new ImageRecylerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ImageShow.class);
                String transitionName = getString(R.string.my_transition);
                intent.putExtra("position", position);
                Log.d(TAG, TAG + position + "name" + data.getImageFiles().get(position).getPath());
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                        (getActivity(), view, transitionName);

                ActivityCompat.startActivity(getActivity(), intent, activityOptionsCompat.toBundle());
            }
        });
        viewAdapter.setOnItemLongClickListener(new ImageRecylerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int Position, final List<ImageFiles> list) {
                final ConfirmDialog confirmDialog = new ConfirmDialog(getActivity(), "是否删除文件");
                confirmDialog.setConfirmDialogOnClick(new ConfirmDialog.ConfirmDialogOnClick() {
                    @Override
                    public void onClickOK() {
                        new File(list.get(Position).getPath()).delete();
                        data.getImageFiles().remove(Position);
                        try {
                            MyDbService.getInterest().removeImage(list.get(Position));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        viewAdapter.getData(data.getImageFiles());
                        confirmDialog.dismiss();

                    }
                });
                confirmDialog.show();
            }

        });

       /* recyclerView.setOnItemScrollChangeListener(new MyRecycler.OnItemScrollChangeListener() {
            @Override
            public void onChange(View view, int position) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }
        });*/

    }
}
