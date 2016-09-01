package yuan.com.luoling.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by yuan-pc on 2016/06/29.
 */

@Table(name = "ImageFile")
public class ImageFile implements Serializable {
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
    public ImageFile() {
    }

    public ImageFile(int id, String path, String name, long size, long time) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", time=" + time +
                '}';
    }
}
