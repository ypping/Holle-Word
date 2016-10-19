package yuan.com.luoling;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YUAN on 2016/10/11.
 */

public class ExceptionHandling implements Thread.UncaughtExceptionHandler {
    public final String TAG = "ExceptionHandling";
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    /**
     * ExceptionHandling的实例
     */
    private static ExceptionHandling INSTANCE;
    /**
     * 程序的content对象
     */
    private Context context;
    /**
     * 用来储存设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<>();
    /**
     * 用于格式化日期，作为日志文件的一部分
     */
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private ExceptionHandling() {

    }

    /**
     * 使用单例模式，保证是有一个实例存在
     *
     * @return INSTANCE 本类的对象
     */
    public static ExceptionHandling getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExceptionHandling();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
        //获取系统默认的uncaughtExeption处理器
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该处理器为程序默认的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 但UncaughtException发生时会转入该函数来处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //TODO 这个地方需要加上“！”符号，不然全局异常拦截做不到
        if (handleException(ex) && uncaughtExceptionHandler != null) {
            //如果用户没有处理则让系统默认处理
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "一个错误发生时收集包信息：休眠", e);
            }
            ActivityManagement.getInstance().finishAllActivity();
            //结束进程
            android.os.Process.killProcess(android.os.Process.myPid());
            //退出程序
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "非常遗憾，程序崩溃了，即将退出。。。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        //手机设备参数信息
        collectDeviceInfo(context);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "一个错误发生时收集包信息：文件名", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + "::::" + field.get(null));
            } catch (IllegalAccessException e) {
                Log.e(TAG, "一个错误发生时收集包信息：读取", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名，便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timstamp = System.currentTimeMillis();
            String time = format.format(new Date());
            String fileName = "crash-" + time + "-" + timstamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/yuan.com.luoling/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "一个错误发生时收集包信息：文件未找到", e);
        } catch (IOException e) {
            Log.e(TAG, "一个错误发生时收集包信息：io流异常", e);
        }
        return null;
    }
}
