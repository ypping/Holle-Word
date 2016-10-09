package yuan.com.luoling.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.VideoFiles;
import yuan.com.luoling.ui.activity.VideoActivity;
import yuan.com.luoling.ui.adapter.VideoRecylerAdapter;
import yuan.com.luoling.ui.widget.SpacesItemDecoration;
import yuan.com.luoling.utils.DensityUtil;

/**
 * 视频界面
 */
public class VideoFragment extends Fragment {
    private final String TAG = "VideoFragment";
    private RecyclerView recyclerView;
    private VideoRecylerAdapter videoRecylerAdapter;
    private ListDate data = ListDate.getListData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.videoRecyleView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(DensityUtil.dip2px(getActivity(), 5));
        recyclerView.addItemDecoration(decoration);
        videoRecylerAdapter = new VideoRecylerAdapter(getActivity(), data);
        /**
         * item的点击事件
         */
        videoRecylerAdapter.setOnItemClickListener(new VideoRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("VideoFiles", (Serializable) data.getVideoFiles());
                startActivity(intent);
            }
        });
        videoRecylerAdapter.setOnItemLongClickListener(new VideoRecylerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int Position, List<VideoFiles> list) {

            }

        });
        recyclerView.setAdapter(videoRecylerAdapter);
        TextView textView = (TextView) view.findViewById(R.id.text);
        if (data.getVideoFiles().size() < 1) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
