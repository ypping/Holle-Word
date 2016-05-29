package yuan.com.luoling;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import yuan.com.luoling.bean.LJImage;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends Activity {
    private final int INTERNET_WIFI = 1;
    private final int INTERNET_CMWAP = 2;
    private final int INTERNET_CMNET = 3;

    @ViewInject(value = R.id.main_image)
    private ImageView imageView;
    @ViewInject(value = R.id.main_image2)
    private ImageView image2;

    String uri = "http://b214.photo.store.qq.com/psb?/89a3bb81-709c-4251-9f09-b09ed0d423e4/e9tmbvuZDTuLk*W9uqtDS2vMlYCr*ADo24i9obd5zIk!/b/YcWsoX..IgAAYkCTm3.AGwAA&bo=ngL2AQAAAAABA08!&rf=viewer_4";
    String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM" + File.separator;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        boolean br = isInternet();
        Log.i("braaa", "" + br);
        int intenttype = getInternetType();
        Log.i("intentType", "intent:" + intenttype);
        setImage();
        setDB();

    }

    private void setDB() {
        DbManager db = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig());
        try {
            List<LJImage> list = db.selector(LJImage.class).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void setImage() {

        /*Picasso.with(getApplicationContext()).load(uri)
                .placeholder(R.drawable.common_full_open_on_phone)
                .error(R.drawable.common_ic_googleplayservices)
                .fit().tag(getApplicationContext()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });*/

        x.image().bind(image2, "http://pic.baike.soso.com/p/20090711/20090711101754-314944703.jpg");
    }

    public void downLodn(View view) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

        }

    }

    private void setFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final File file = new File(filePath);
            Log.i("file", "setFile: " + file.getParentFile().exists());
            if (!file.getParentFile().exists()) {

                try {
                    file.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL(uri);
                            InputStream inputStream = url.openStream();
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            int line = 0;
                            byte[] b = new byte[1024];
                            while ((line = inputStream.read(b)) > 0) {
                                fileOutputStream.write(b, 0, line);
                                Log.d("line", "run: " + line);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    /**
     * 判断是否有网络
     *
     * @return
     */
    public boolean isInternet() {
        ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * 判断网络的类型
     *
     * @return
     */
    public int getInternetType() {
        int netType = 0;
        ConnectivityManager connection = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();
        if (networkInfo == null) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String info = networkInfo.getExtraInfo();
            if (info.toLowerCase().equals("3gnet")) {
                netType = 3;
                Log.i("infoto", "info:" + info.toLowerCase().toLowerCase());
            } else {
                netType = 2;
                Log.i("infoto", "info:" + info.toLowerCase().toLowerCase());
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }

        return netType;
    }
}
