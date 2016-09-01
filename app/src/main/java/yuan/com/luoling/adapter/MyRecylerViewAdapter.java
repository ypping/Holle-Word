package yuan.com.luoling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFile;
import yuan.com.luoling.bean.ListDate;

/**
 * Created by yuan-pc on 2016/06/24.
 */
public class MyRecylerViewAdapter extends RecyclerView.Adapter<MyRecylerViewAdapter.MyViewHolder> {
    private final String TAG = "MyRecylerViewAdapter";
    private Context context;
    private List<?> list;
    private ListDate date ;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private OnItemShow onItemShow;
    private Object tag;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemShow {
        void onItemShow(View view, int position);
    }

    public void setOnItemShow(OnItemShow onItemShow) {
        this.onItemShow = onItemShow;
    }

    public MyRecylerViewAdapter(Context context, ListDate date, Object tag) {
        this.context = context;
        if (date.getImageFiles() != null) {
            list = date.getImageFiles();
            this.tag = tag;
        } else {
            date.setImageFiles(new ArrayList<ImageFile>());
            list = date.getImageFiles();
        }
        this.date = date;
        inflater = LayoutInflater.from(context);
    }

    public MyRecylerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (date.getImageFiles() != null) {
            list = date.getImageFiles();
        } else {
            date.setImageFiles(new ArrayList<ImageFile>());
            list = date.getImageFiles();
        }

    }

    public void getData(ListDate date) {
        this.date = date;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_view, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.item_list_image);
        viewHolder.textView = (TextView) view.findViewById(R.id.item_list_text);

        return viewHolder;
    }

    /**
     * 控件的绑定方法
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        File file = new File(date.getImageFiles().get(position).getPath());
        Log.i(TAG, "file:" + file.getPath());
        Picasso.with(context).load(file).fit().tag(tag).placeholder(R.mipmap.summer_icon).into(holder.imageView);
        holder.textView.setText(date.getImageFiles().get(position).getName());
        Log.i("imageFiles", date.getImageFiles().get(position).getName());


        /**
         * 每个item的点击事件
         */
        if (onItemClickListener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        if (onItemShow != null) {
            if (!holder.itemView.isShown()) {
                Toast.makeText(context, "没有显示", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            date.setImageFiles(new ArrayList<ImageFile>());
        }
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;


        public MyViewHolder(View itemView) {

            super(itemView);
        }
    }
}
