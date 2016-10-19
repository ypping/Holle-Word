package yuan.com.luoling;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


/**
 * Created by YUAN on 2016/9/6.
 */
public class BaseActivity extends Activity {
    private View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
