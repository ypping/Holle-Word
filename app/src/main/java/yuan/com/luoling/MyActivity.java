package yuan.com.luoling;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yuan.com.luoling.adapter.ListAdapter;

/**
 * Created by yuan-pc on 2016/05/23.
 */
public class MyActivity extends Activity {
    ListView list;
    List<File> lis;
    ListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
        lis = new ArrayList<>();
        findFilePath4(Environment.getExternalStorageDirectory().toString()
                + File.separator + "DCIM" + File.separator);

        Log.i("ffffff", "file" + lis.size());
        adapter = new ListAdapter(getApplicationContext(), lis);
        list.setAdapter(adapter);
    }

    private void initView() {
        list = (ListView) findViewById(R.id.list_view_my);
    }

    public void findFilePath4(String path) {

        Log.i("path     ", "" + path);
        File[] a = new File(path).listFiles();

        File temp = null;

        for (int i = 0; i < a.length; i++) {
            temp = a[i];

            if (temp.isFile()) {
                lis.add(temp);
                Log.i("list", "file>>" + lis.size());
            } else {
                findFilePath4(temp.getPath());
            }
        }

    }
}
