package yuan.com.luoling.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 图片类
 * Created by yuan-pc on 2016/06/29.
 */

@Table(name = "ImageFiles", onCreated = "CREATE UNIQUE INDEX luoLing_Image ON ImageFiles(path)")
public class ImageFiles implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "path", property = "NOT NULL")
    private String path;
    @Column(name = "name", property = "NOT NULL")
    private String name;
    @Column(name = "size")
    private Long size;
    @Column(name = "time", property = "NOT NULL")
    private long time;
    @Column(name = "Image_ID", property = "NOT NULL")
    private int Image_ID;

    public ImageFiles() {
    }

    public ImageFiles(int Image_ID, String path, String name, long size, long time) {
        this.Image_ID = Image_ID;
        this.path = path;
        this.name = name;
        this.size = size;
        this.time = time;

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

    public int getImage_ID() {
        return Image_ID;
    }

    public void setImage_ID(int image_ID) {
        Image_ID = image_ID;
    }

    @Override
    public String toString() {
        return "ImageFiles{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", time=" + time +
                ", Image_ID=" + Image_ID +
                '}';
    }
}
