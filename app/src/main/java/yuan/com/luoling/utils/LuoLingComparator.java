package yuan.com.luoling.utils;

import java.util.Comparator;

import yuan.com.luoling.bean.ImageFiles;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.bean.VideoFiles;

/**
 * 排序类，将list由大到小进行排序
 * Created by YUAN on 2016/9/1.
 */
public class LuoLingComparator implements Comparator {
    private Class aClass;

    public LuoLingComparator(Class aClass) {
        this.aClass = aClass;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        int i = 0;
        if (aClass.getName().equals(ImageFiles.class.getName())) {
            ImageFiles image = (ImageFiles) lhs;
            ImageFiles imageFiles = (ImageFiles) rhs;
            //首先比较大小，如果年龄相同，则比较名字
            int flag = image.getSize().compareTo(imageFiles.getSize());
            if (flag == 0) {
                i = image.getName().compareTo(imageFiles.getName());
            } else {
                i = flag;
            }
        } else if (aClass.getName().equals(MusicFiles.class.getName())) {
            MusicFiles music = (MusicFiles) lhs;
            MusicFiles musicFiles = (MusicFiles) rhs;

            //首先比较大小，如果大小相同，则比较名字

            int flag = music.getSize().compareTo(musicFiles.getSize());
            if (flag == 0) {
                i = music.getName().compareTo(musicFiles.getName());
            } else {
                i = flag;
            }
        } else if (aClass.getName().equals(VideoFiles.class.getName())) {
            VideoFiles video = (VideoFiles) lhs;
            VideoFiles videoFiles = (VideoFiles) rhs;

            //首先比较大小，如果大小相同，则比较名字

            int flag = video.getSize().compareTo(videoFiles.getSize());
            if (flag == 0) {
                i = video.getName().compareTo(videoFiles.getName());
            } else {
                i = flag;
            }
        }
        return i;
    }
}

