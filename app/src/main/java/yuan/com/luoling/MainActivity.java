package yuan.com.luoling;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yuan.com.luoling.bean.ImageFile;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.db.MyDbService;
import yuan.com.luoling.inter.ComparatorImage;
import yuan.com.luoling.inter.UpDataImageIm;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private final int INTERNET_WIFI = 1;
    private final int INTERNET_CMWAP = 2;
    private final int INTERNET_CMNET = 3;
    private MyHandler handler;
    @ViewInject(value = R.id.main_image)
    private ImageView imageView;
    @ViewInject(value = R.id.main_image2)
    private ImageView image2;
    @ViewInject(value = R.id.main_text)
    private CheckBox textView;
    String uri = "http://b214.photo.store.qq.com/psb?/89a3bb81-709c-4251-9f09-b09ed0d423e4/e9tmbvuZDTuLk*W9uqtDS2vMlYCr*ADo24i9obd5zIk!/b/YcWsoX..IgAAYkCTm3.AGwAA&bo=ngL2AQAAAAABA08!&rf=viewer_4";
    private List<ImageFile> imageFiles = new ArrayList<ImageFile>();
    private MyDbService myDbService = MyDbService.getInterest();
    private ListDate date = ListDate.getListData();
    private Cursor cursor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        boolean br = isInternet();
        handler = new MyHandler();
        initWebView();
        setData();
        int intenttype = getInternetType();
        Log.i("intentType", "intent:" + intenttype);
        setImage();

    }

    private void setData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.getApp().isFristRun()) {
                    findFilePath4(Environment.getExternalStorageDirectory().getPath());
                    Log.e(TAG, TAG + ":" + Environment.getExternalStorageDirectory().getPath());
                    handler.sendEmptyMessage(1);
                    MyApplication.getApp().onFirist();
                } else {
                    try {
                        List<ImageFile> files = new ArrayList<ImageFile>();
                        files.addAll(MyApplication.getApp().getDbManager().findAll(ImageFile.class));
                        //TODO 第一次装APP删除
                        ComparatorImage comparatorImage = new ComparatorImage();
                        Collections.sort(files, comparatorImage);
                        Collections.reverse(files);
                        date.setImageFiles(files);
                        // UpDataImageIm.getInstance().getUpDataImageFile().upDataImage();
                        Log.e(TAG, TAG + ":" + date.getImageFiles().size());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    int i = 0;

    /**
     * 遍历所有文件夹下的图片文件
     *
     * @param path
     */
    public void findFilePath4(String path) {

        File[] a = new File(path).listFiles();

        for (File f : a) {
            if (f.isFile()) {
                if (f.getName().toLowerCase().endsWith(".jpg")) {
                    ImageFile imageFile = new ImageFile(i, f.getPath(), f.getName(), f.length(), f.lastModified());
                    imageFiles.add(imageFile);
                    i++;
                } else if (f.getName().toLowerCase().endsWith(".png")) {
                    ImageFile imageFile = new ImageFile(i, f.getPath(), f.getName(), f.length(), f.lastModified());
                    imageFiles.add(imageFile);
                    i++;
                } else if (f.getName().toLowerCase().endsWith(".jpeg")) {
                    ImageFile imageFile = new ImageFile(i, f.getPath(), f.getName(), f.length(), f.lastModified());
                    imageFiles.add(imageFile);
                    i++;
                } else if (f.getName().toLowerCase().endsWith(".bmp")) {
                    ImageFile imageFile = new ImageFile(i, f.getPath(), f.getName(), f.length(), f.lastModified());
                    imageFiles.add(imageFile);
                    i++;
                } else if (f.getName().toLowerCase().endsWith(".gif")) {
                    ImageFile imageFile = new ImageFile(i, f.getPath(), f.getName(), f.length(), f.lastModified());
                    imageFiles.add(imageFile);
                    i++;
                }
            } else {
                Log.e(TAG, TAG + ":" + f.getPath() + "==" + i);
                findFilePath4(f.getPath());
            }
        }
    }

    /**
     * 用游标查询媒体库，获得图片信息
     */
    private void findImage() {
        cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.SIZE
                }, null, null, null);
        cursor.moveToFirst();
        Log.e(TAG, TAG + "=" + "完成");
        handler.sendEmptyMessage(1);
    }

    private void addImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* for (int i = 0; i < cursor.getCount(); i++) {
                    if (!TextUtils.isEmpty(cursor.getString(4)) && !TextUtils.isEmpty(cursor.getString(1))) {
                        ImageFile imageFile = new ImageFile(cursor.getString(4), cursor.getString(1), cursor.getLong(5), cursor.getLong(3), cursor.getLong(2));
                        imageFiles.add(imageFile);
                    }
                    date.setImageFiles(imageFiles);
                    cursor.moveToNext();
                }*/
                Log.e(TAG, TAG + "  " + imageFiles.size());
                try {
                    ComparatorImage comparatorImage = new ComparatorImage();
                    Collections.sort(imageFiles, comparatorImage);
                    Collections.reverse(imageFiles);
                    myDbService.addImageDB(imageFiles);
                    Log.e(TAG, TAG + "mdd" + imageFiles.size());
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    date.setImageFiles(imageFiles);
                    addImage();
                    break;
                case 200:

                    break;
            }
        }
    }

    boolean isshow = false;

    /**
     * 事件的绑定
     */
    private void initWebView() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isshow) {
                    UpDataImageIm.getInstance().getUpDataImageFile().upDataImage();
                }
                isshow = true;
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);

            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setImage() {

        x.image().bind(imageView, uri);
        x.image().bind(image2, "http://pic.baike.soso.com/p/20090711/20090711101754-314944703.jpg");
    }

    public void downLodn(View view) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

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
