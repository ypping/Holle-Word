package yuan.com.luoling.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.R;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.VideoFiles;
import yuan.com.luoling.services.VideoServices;
import yuan.com.luoling.ui.activity.HomeActivity;
import yuan.com.luoling.ui.activity.VideoActivity;
import yuan.com.luoling.ui.adapter.VideoRecyclerAdapter;
import yuan.com.luoling.ui.widget.SpacesItemDecoration;
import yuan.com.luoling.utils.DensityUtil;
import yuan.com.luoling.widget.pulltorefres.library.extras.recyclerview.PullToRefreshRecyclerView;

/**
 * 视频界面
 */
public class VideoFragment extends Fragment {
    private final String TAG = "VideoFragment";
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private VideoRecyclerAdapter videoRecyclerAdapter;
    private ListDate data = ListDate.getListData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        pullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.videoRecyleView);
        RecyclerView recyclerView = pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(DensityUtil.dip2px(getActivity(), 5));
        recyclerView.addItemDecoration(decoration);
        videoRecyclerAdapter = new VideoRecyclerAdapter(getActivity(), data);
        /**
         * item的点击事件
         */
        videoRecyclerAdapter.setOnItemClickListener(new VideoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("VideoFiles", (Serializable) data.getVideoFiles());
                startActivity(intent);
            }
        });
        videoRecyclerAdapter.setOnItemLongClickListener(new VideoRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int Position, List<VideoFiles> list) {

            }

        });
        recyclerView.setAdapter(videoRecyclerAdapter);
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
                videoRecyclerAdapter.getData(data.getVideoFiles());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        if (MyApplication.getApp().getVideoServices() != null) {
            getActivity().unbindService(serviceConnection);
        }
        super.onDestroy();
    }

    /**
     * 服务链接方式
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyApplication.getApp().setVideoServices((VideoServices) ((VideoServices.VideoBind) service).getServices());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyApplication.getApp().setVideoServices(null);
        }
    };
}
