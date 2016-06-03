package yuan.com.luoling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.adapter.ListAdapter;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.ui.ImageShow;

/**
 * Created by yuan-pc on 2016/05/23.
 */
public class MyActivity extends Activity {
    private ListView list;
    private List<File> lis;
    private ListAdapter adapter;

    private ListDate data = ListDate.getListData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();

        Log.i("ffffff", "file" + lis.size());
        adapter = new ListAdapter(MyActivity.this);
        Log.i("myActivity:date", "" + data.getList().size());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra("id", position);
                intent.setClass(MyActivity.this, ImageShow.class);

                Toast.makeText(MyActivity.this, "" + lis.get(position).getPath(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {

        list = (ListView) findViewById(R.id.list_view_my);
        lis = new ArrayList<>();


    }

}
