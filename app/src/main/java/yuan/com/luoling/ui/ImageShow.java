package yuan.com.luoling.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ListDate;

/**
 * Created by yuan-pc on 2016/06/01.
 */
@ContentView(value = R.layout.image_show)
public class ImageShow extends Activity {
    //从myActivity获得的数据加载到本页面形成一个可以滑动的页面
    private List<File> list;

    private ListDate data;
    //绑定页面与资源id
    @ViewInject(value = R.id.image_show_image)
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        data = ListDate.getListData();
        this.list = data.getList();
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("id");
        x.image().bind(image, list.get(position).getPath());
    }

    private void initView() {

    }


}
