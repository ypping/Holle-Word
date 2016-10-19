package yuan.com.luoling.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ImageFiles;

/**
 * recyclerViewAdapter瀑布流布局适配器
 * Created by yuan-pc on 2016/06/24.
 */
public class ImageRecylerViewAdapter extends RecyclerView.Adapter<ImageRecylerViewAdapter.MyViewHolder> {
    private final String TAG = "ImageRecyclerViewAdapter";
    private Context context;
    private List<ImageFiles> list;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int Position, List<ImageFiles> list);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public ImageRecylerViewAdapter(Context context, List<ImageFiles> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public ImageRecylerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void getData(List<ImageFiles> list) {
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
        final File file = new File(list.get(position).getPath());

        final ViewPropertyAnimation.Animator animator = new ViewPropertyAnimation.Animator() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void animate(final View view) {
                view.animate().alphaBy(0.1f);
                /**
                 * 设置图片的缩放比例
                 */
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.3f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(3000);
                scaleAnimation.setFillEnabled(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.8f);
                alphaAnimation.setDuration(3000);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                view.setAnimation(animationSet);
                view.animate().start();
            }
        };
        Glide.with(context).load(file).skipMemoryCache(false).fitCenter().animate(animator).
                placeholder(R.mipmap.moren).into(holder.imageView);
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

        if (onItemLongClickListener != null)
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(v, position, list);
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            list = new ArrayList<ImageFiles>();
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
