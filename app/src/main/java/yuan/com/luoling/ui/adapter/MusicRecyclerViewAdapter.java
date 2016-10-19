package yuan.com.luoling.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.ui.fragment.MusicFragment.OnListFragmentInteractionListener;
import yuan.com.luoling.utils.FileUtils;
import yuan.com.luoling.utils.TimeUtils;

/**
 * 音乐列表适配器
 * TODO: Replace the implementation with code for your data type.
 */
public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<MusicRecyclerViewAdapter.ViewHolder> {

    private List<MusicFiles> mValues;
    private OnListFragmentInteractionListener mListener;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setData(List<MusicFiles> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int Position, List<MusicFiles> list);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public MusicRecyclerViewAdapter(Context context, List<MusicFiles> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        this.context = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.musicSinger.setText(mValues.get(position).getArtist());
        holder.musicName.setText(mValues.get(position).getName());
        Glide.with(context).load(mValues.get(position).getArtistPhotoURL()).placeholder(R.mipmap.moren).
                fitCenter().centerCrop().into(holder.musicImage);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        if (mValues.get(position).getTime() == 0) {

        }
        holder.musicTime.setText(String.valueOf(TimeUtils.milliseconds2String(mValues.get(position).getTime(), simpleDateFormat)));
        holder.musicSize.setText(String.valueOf(FileUtils.getCacheLengthStr(mValues.get(position).getSize())));
        holder.musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.musicView, position);
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.musicFiles);
                }
            }
        });
        holder.musicView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClick(holder.musicView, position, mValues);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            mValues = new ArrayList<>();
        }
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView musicImage;
        private TextView musicName;
        private TextView musicSinger;
        private TextView musicTime;
        private MusicFiles musicFiles;
        private TextView musicSize;
        private View musicView;

        public ViewHolder(View view) {
            super(view);
            musicImage = (ImageView) view.findViewById(R.id.musicImage);
            musicName = (TextView) view.findViewById(R.id.musicName);
            musicSinger = (TextView) view.findViewById(R.id.musicSinger);
            musicTime = (TextView) view.findViewById(R.id.musicTime);
            musicView = view.findViewById(R.id.musicView);
            musicSize = (TextView) view.findViewById(R.id.musicSize);
        }
    }

}
