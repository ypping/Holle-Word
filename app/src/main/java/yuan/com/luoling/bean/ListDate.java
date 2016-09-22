package yuan.com.luoling.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据类，采用单例模式
 * Created by yuan-pc on 2016/06/01.
 */
public class ListDate {
    private static ListDate date;

    public static ListDate getListData() {
        if (date == null) {
            date = new ListDate();
        }

        return date;
    }

    /**
     * 在当前app中Image Files的一个集合；
     */
    private List<ImageFiles> imageFiles;
    /**
     * 在当前app中Music Files的一个集合；
     */
    private List<MusicFiles> musicFiles;
    /**
     * 在当前app的Video Files的一个集合；
     */
    private List<VideoFiles> videoFiles;

    public List<ImageFiles> getImageFiles() {
        if (imageFiles == null) {
            imageFiles = new ArrayList<>();
        }
        return imageFiles;
    }

    public void setImageFiles(List<ImageFiles> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public List<MusicFiles> getMusicFiles() {
        if (musicFiles == null) {
            musicFiles = new ArrayList<>();
        }
        return musicFiles;
    }

    public void setMusicFiles(List<MusicFiles> musicFiles) {
        this.musicFiles = musicFiles;
    }

    public List<VideoFiles> getVideoFiles() {
        return videoFiles;
    }

    public void setVideoFiles(List<VideoFiles> videoFiles) {
        this.videoFiles = videoFiles;
    }
}
