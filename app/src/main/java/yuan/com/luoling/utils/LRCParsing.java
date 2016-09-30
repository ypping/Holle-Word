package yuan.com.luoling.utils;

import android.util.Log;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yuan.com.luoling.bean.LrcData;

/**
 * 歌词文件的解析类
 * Created by YUAN on 2016/9/27.
 */

public class LRCParsing {

    public InputStream readLrc(String path) throws FileNotFoundException {
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        return inputStream;
    }

    public LrcData parser(String path) throws IOException {
        InputStream in = readLrc(path);
        LrcData lrcinfo = new LrcData();
        rarserLrc(in, lrcinfo, path);
        return lrcinfo;

    }

    /**
     * 将输入流用于写入输入信息
     *
     * @param inputStream
     * @param lrcData
     * @param path        判断文件编码
     * @return
     */
    public LrcData rarserLrc(InputStream inputStream, LrcData lrcData, String path) {
        try {
            UniversalDetector detector = new UniversalDetector(null);
            byte buf[] = new byte[4096];
            int len = 0;
            ArrayList<Byte> buffer = new ArrayList<Byte>();
            while ((len = inputStream.read(buf)) != -1 && !detector.isDone()) {
                detector.handleData(buf, 0, len);
                for (int i = 0; i < len; i++) {
                    buffer.add(buf[i]);
                }
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            detector.reset();
            Log.i("UniversalDetector", "encoding:" + encoding);
            byte[] d = new byte[buffer.size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = buffer.get(i);
            }
            String data = new String(d, encoding);
            String lrc[] = data.split("[[\r\n][\n]]");
            for (String lin : lrc) {
                decodeLine(lin, lrcData);
            }
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcData;
    }

    /**
     * 利用正则表达式解析每行具体语句 并在解析完该语句后，将解析出来的信息设置在LrcData对象中
     *
     * @param str     每行的内容
     * @param lrcData
     */
    private void decodeLine(String str, LrcData lrcData) {
        //获得歌曲信息
        if (str.startsWith("[ti:")) {
            String name = str.substring(4, str.length() - 1);
            lrcData.setMusicName(name);
            //获得歌手信息
        } else if (str.startsWith("[ar:")) {
            String singer = str.substring(4, str.length() - 1);
            lrcData.setSinger(singer);
            //获得专辑信息
        } else if (str.startsWith("[al")) {
            String album = str.substring(4, str.length() - 1);
            lrcData.setAlbum(album);
        }// 通过正则取得每句歌词信息
        else {
            // 设置正则规则
            String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2,})\\]";
            // 编译
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);
            Integer currentTime = -1;
            String currentContent = "";
            // 如果存在匹配项，则执行以下操作
            while (matcher.find()) {
                // 得到这个匹配项中的组数
                int groupCount = matcher.groupCount();

                // 得到每个组中内容
                for (int i = 0; i <= groupCount; i++) {
                    String timeStr = matcher.group(i);
                    if (i == 1) {
                        // 将第二组中的内容设置为当前的一个时间点
                        currentTime = strToLong(timeStr);
                    }
                }

                // 得到时间点后的内容
                String[] content = pattern.split(str);

                // 输出数组内容
                for (int i = 0; i < content.length; i++) {
                    if (i == content.length - 1) {
                        // 将内容设置为当前内容
                        currentContent = content[i];
                    }
                }
                // 设置时间点和内容的映射
                lrcData.getLyrics().put(currentTime, currentContent);
                System.out.println("put---currentTime--->" + currentTime + "----currentContent---->" + currentContent);
            }
        }
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     *
     * @param timeStr 字符形式的时间点
     * @return int 形式的毫秒时间
     */
    private Integer strToLong(String timeStr) {
        // 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        // 1:使用：分割 2：使用.分割
        String[] s = timeStr.split(":");
        int min = Integer.parseInt(s[0]);
        float sec = Float.parseFloat(s[1]);
        return (int) (min * 60 * 1000 + sec * 1000);
    }
}
