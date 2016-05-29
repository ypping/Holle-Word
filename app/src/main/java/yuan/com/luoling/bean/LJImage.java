package yuan.com.luoling.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by yuan-pc on 2016/05/24.
 */
@Table(name = "LJImage")
public class LJImage {
    @Column(name = "id")
    private int id;
    @Column(name = "creatTime")
    private int creatTime;
    @Column(name = "updateTime")
    private int updateTime;
    @Column(name = "name")
    private String name;
    @Column(name = "imageSize")
    private int imageSize;

    public int getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(int creatTime) {
        this.creatTime = creatTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
