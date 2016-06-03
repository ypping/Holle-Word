package yuan.com.luoling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Other extends Activity {
    private Button button1;
    private TextView textView1, textView2, textView3, textView4, textView5;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);

        textView1 = (TextView) findViewById(R.id.text);
        intent = getIntent();
        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("qq", 123);
                setResult(0x717, intent);
                finish();
            }
        });


        String first = intent.getStringExtra("editText1");
        String second = intent.getStringExtra("editText2");
        String third = intent.getStringExtra("editText3");
        String fourth = intent.getStringExtra("editText4");

        int result = Integer.parseInt(first) + Integer.parseInt(second) + Integer.parseInt(third) + Integer.parseInt(fourth);

        textView1.setText("" + String.valueOf(result));

    }

}


