package yuan.com.luoling;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JustActivity extends AppCompatActivity {
    private EditText editText1, editText2, editText3, editText4;
    private TextView textView1, textView2, textView3, textView4, textView5;
    private Button button;
    final int CODE = 0x717;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jusst);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editText1String = editText1.getText().toString();
                String editText2String = editText2.getText().toString();
                String editText3String = editText3.getText().toString();
                String editText4String = editText4.getText().toString();
                if (TextUtils.isEmpty(editText1String)) {
                    editText1String = "0";
                }
                if (TextUtils.isEmpty(editText2String)) {
                    editText2String = "0";
                }
                if (TextUtils.isEmpty(editText3String)) {
                    editText3String = "0";
                }
                if (TextUtils.isEmpty(editText4String)) {
                    editText4String = "0";
                }
                Intent intent = new Intent(JustActivity.this, Other.class);
                intent.putExtra("editText1", editText1String);
                intent.putExtra("editText2", editText2String);
                intent.putExtra("editText3", editText3String);
                intent.putExtra("editText4", editText4String);

                startActivityForResult(intent, CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, data.getExtras().getInt("qq") + "", Toast.LENGTH_SHORT).show();
    }
}
