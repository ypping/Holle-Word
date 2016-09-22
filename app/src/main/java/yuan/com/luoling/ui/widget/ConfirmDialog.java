package yuan.com.luoling.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import yuan.com.luoling.R;

public class ConfirmDialog extends Dialog {

    View commit;
    View cancle;
    TextView contenttv;
    Context context;
    String text;

    public ConfirmDialog(Context context, String text) {
        super(context, R.style.MyDialogStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.text = text;
    }

    public ConfirmDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cleencache);
        setCancelable(true);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8);
        lp.height = LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        commit = findViewById(R.id.commit);
        cancle = findViewById(R.id.cancle);
        contenttv = (TextView) findViewById(R.id.notice_tv);
        contenttv.setText(text);
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mConfirmDialogOnClick != null) {
                    mConfirmDialogOnClick.onClickOK();
                } else {
                    dismiss();
                }
            }
        });
    }

    ConfirmDialogOnClick mConfirmDialogOnClick;

    public void setConfirmDialogOnClick(ConfirmDialogOnClick mConfirmDialogOnClick) {
        this.mConfirmDialogOnClick = mConfirmDialogOnClick;
    }

    public interface ConfirmDialogOnClick {
        public void onClickOK();
    }
}
