package yuan.com.luoling.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 音乐文件类
 * Created by YUAN on 2016/9/12.
 */
@Table(name = "MusicFiles", onCreated = "CREATE UNIQUE INDEX luoLing_Music ON MusicFiles(path)")
public class MusicFiles implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "path", property = "NOT NULL")
    private String path;
    /**
     * 文件名
     */
    @Column(name = "name", property = "NOT NULL")
    private String name;
    /**
     * 歌曲文件大小
     */
    @Column(name = "size")
    private Long size;
    /**
     * 播放时间长度
     */
    @Column(name = "time")
    private long time;
    /**
     * 数据库中musicID
     */
    @Column(name = "Music_ID")
    private int music_ID;
    /**
     * 演唱者/歌手名
     */
    @Column(name = "artist")
    private String artist;
    /**
     * 专辑名
     */
    @Column(name = "album")
    private String album;
    /**
     * 网络加载歌手写真地址
     */
    @Column(name = "artistPhotoURL")
    private String artistPhotoURL;
    /**
     * 本地加载歌手写真地址
     */
    @Column(name = "artistPhotoURLFile")
    private String artistPhotoURLFile;
    /**
     * 歌词文件路径
     */
    @Column(name = "lrcURL")
    private String lrcURL;

    public MusicFiles() {

    }

    public MusicFiles(String path, String name, Long size, long time, int music_ID, String artist,
                      String album, String artistPhotoURL, String artistPhotoURLFile, String lrcURL) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.time = time;
        this.music_ID = music_ID;
        this.artist = artist;
        this.album = album;
        this.artistPhotoURL = artistPhotoURL;
        this.artistPhotoURLFile = artistPhotoURLFile;
        this.lrcURL = lrcURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMusic_ID() {
        return music_ID;
    }

    public void setMusic_ID(int music_ID) {
        this.music_ID = music_ID;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtistPhotoURL() {
        return artistPhotoURL;
    }

    public void setArtistPhotoURL(String artistPhotoURL) {
        this.artistPhotoURL = artistPhotoURL;
    }

    public String getArtistPhotoURLFile() {
        return artistPhotoURLFile;
    }

    public void setArtistPhotoURLFile(String artistPhotoURLFile) {
        this.artistPhotoURLFile = artistPhotoURLFile;
    }

    public String getLrcURL() {
        return lrcURL;
    }

    public void setLrcURL(String lrcURL) {
        this.lrcURL = lrcURL;
    }

    @Override
    public String toString() {
        return "MusicFiles{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", time=" + time +
                ", music_ID=" + music_ID +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", artistPhotoURL='" + artistPhotoURL + '\'' +
                ", artistPhotoURLFile='" + artistPhotoURLFile + '\'' +
                ", lrcURL='" + lrcURL + '\'' +
                '}';
    }
}
