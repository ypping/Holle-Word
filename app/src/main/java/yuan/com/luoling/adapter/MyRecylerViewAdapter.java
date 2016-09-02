package yuan.com.luoling.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFile;
import yuan.com.luoling.bean.ListDate;

/**
 * recyleviewAdapter瀑布流布局适配器
 * Created by yuan-pc on 2016/06/24.
 */
public class MyRecylerViewAdapter extends RecyclerView.Adapter<MyRecylerViewAdapter.MyViewHolder> {
    private final String TAG = "MyRecylerViewAdapter";
    private Context context;
    private List<ImageFile> list;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecylerViewAdapter(Context context, ListDate date) {
        this.context = context;
        if (date.getImageFiles() != null) {
            list = date.getImageFiles();
        } else {
            date.setImageFiles(new ArrayList<ImageFile>());
            list = date.getImageFiles();
        }
        inflater = LayoutInflater.from(context);
    }

    public MyRecylerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void getData(List<ImageFile> list) {
        this.list = list;
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
        File file = new File(list.get(position).getPath());
        Log.i(TAG, "file:" + file.getPath());
        final ViewPropertyAnimation.Animator animator = new ViewPropertyAnimation.Animator() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void animate(final View view) {
                view.animate().setDuration(2000);
                view.animate().rotation(360);
                view.animate().alphaBy(0.5f);
                view.animate().rotationXBy(0.5f);
                view.animate().translationZBy(0.6f);
                view.animate().start();
            }
        };
        Glide.with(context).load(file).skipMemoryCache(false).fitCenter().animate(animator).placeholder(R.mipmap.summer_icon)
                .into(holder.imageView);
        holder.textView.setText(list.get(position).getName());
        Log.i("imageFiles", list.get(position).getName());


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
    }

    @Override
    public int getItemCount() {
        if (list == null) {
          list=new ArrayList<ImageFile>();
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
