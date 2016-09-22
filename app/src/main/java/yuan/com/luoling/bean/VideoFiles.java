package yuan.com.luoling.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 视频类
 * Created by YUAN on 2016/9/18.
 */
@Table(name = "VideoFiles", onCreated = "CREATE UNIQUE INDEX luoLing_Video ON VideoFiles(path)")
public class VideoFiles implements Serializable {
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
     * 数据库中videoID
     */
    @Column(name = "video_ID")
    private int video_ID;
    /**
     * 缩略图
     */
    @Column(name = "thumbnal")
    private String thumbnail;


    public VideoFiles() {

    }

    public VideoFiles( String path, String name, Long size, long time, int video_ID, String thumbnail) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.time = time;
        this.video_ID = video_ID;
        this.thumbnail = thumbnail;
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

    public int getVideo_ID() {
        return video_ID;
    }

    public void setVideo_ID(int video_ID) {
        this.video_ID = video_ID;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "VideoFiles{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", time=" + time +
                ", video_ID=" + video_ID +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
