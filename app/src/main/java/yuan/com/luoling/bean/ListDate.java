package yuan.com.luoling.bean;

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

    private List<ImageFile> imageFiles;

    public List<ImageFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<ImageFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
