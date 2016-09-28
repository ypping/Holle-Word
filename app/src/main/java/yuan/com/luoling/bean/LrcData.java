package yuan.com.luoling.bean;

import java.io.IOException;
import java.util.TreeMap;

import yuan.com.luoling.utils.LRCParsing;

/**
 * 一个歌词文件的基本类
 * Created by YUAN on 2016/9/27.
 */

public class LrcData {
    /**
     * 歌曲名字
     */
    private String musicName;
    /**
     * 演唱者
     */
    private String singer;
    /**
     * 歌曲专辑
     */
    private String album;
    /**
     * 保存时间和歌词一一对应
     */
    private TreeMap<Integer, String> lyrics = new TreeMap<>();

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public TreeMap<Integer, String> getLyrics() {
        return lyrics;
    }

    public void setLyrics(TreeMap<Integer, String> lyrics) {
        this.lyrics = lyrics;
    }

    public static LrcData loadLRCFile(String path) throws IOException {
        return new LRCParsing().parser(path);
    }

}
