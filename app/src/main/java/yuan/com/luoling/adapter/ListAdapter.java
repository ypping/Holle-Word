package yuan.com.luoling.adapter;

import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.tsz.afinal.FinalBitmap;

import org.xutils.x;

import java.io.File;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ListDate;

/**
 * Created by yuan-pc on 2016/05/22.
 */
public class ListAdapter extends BaseAdapter {
    List file;
    Context context;
    LayoutInflater inaflater;
    ListDate date = ListDate.getListData();
    FinalBitmap finalBitmap;

    public ListAdapter() {
    }

    public ListAdapter(Context context) {
        this.context = context;
        finalBitmap = FinalBitmap.create(context);
        inaflater = LayoutInflater.from(context);
    }

    public ListAdapter(Context context, List file) {
        this.context = context;
        this.file = file;
        inaflater = LayoutInflater.from(context);
    }

    public void getData() {
        // this.file = file;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return date.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return date.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inaflater.inflate(R.layout.item_list_view, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_list_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(new File(date.getList().get(position).toString())).into(holder.imageView);
        // x.image().bind(holder.imageView, date.getList().get(position).toString());
        //finalBitmap.display(holder.imageView, date.getList().get(position).toString());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;

        public ViewHolder() {

        }
    }
}
