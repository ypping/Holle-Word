package yuan.com.luoling.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import yuan.com.luoling.R;

/**
 * Created by yuan-pc on 2016/05/22.
 */
public class ListAdapter extends BaseAdapter {
    List file;
    Context context;
    LayoutInflater inaflater;

    public ListAdapter() {
    }

    public ListAdapter(Context context, List file) {
        this.context = context;
        this.file = file;
        inaflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return file.size();
    }

    @Override
    public Object getItem(int position) {
        return file.get(position);
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
        Log.i("file", "file:" + file.get(position).toString());
        Picasso.with(context).load(new File(file.get(position).toString())).into(holder.imageView);

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;

        public ViewHolder() {

        }
    }
}
