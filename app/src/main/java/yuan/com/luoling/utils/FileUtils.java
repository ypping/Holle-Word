package yuan.com.luoling.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yuan-pc on 2016/06/03.
 */
public class FileUtils {
    public static void setFile(String filePath, final String uri) {
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

    public static void getFile(final String filePath, final String uri, final Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final File file = new File(filePath);
            if (file.getParentFile().exists()) {
                if (file.isFile()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(uri);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoInput(true);
                                connection.setDoOutput(true);
                                connection.setRequestMethod("POST");
                                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                                FileInputStream inputStream = new FileInputStream(file);
                                int buffSize = 1024;
                                byte[] buff = new byte[buffSize];
                                int length = -1;
                                while ((length = inputStream.read()) != -1) {
                                    outputStream.write(buff, 0, length);
                                }
                                inputStream.close();
                                outputStream.flush();
                                InputStream inputStream1 = connection.getInputStream();
                                int ch;
                                StringBuffer buffer = new StringBuffer();
                                while ((ch = inputStream1.read()) != -1) {
                                    buffer.append((char) ch);
                                }
                                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();

                            } catch (MalformedURLException e) {
                                Toast.makeText(context, "上传失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(context, "上传失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();
                }
            }
        }
    }
}
