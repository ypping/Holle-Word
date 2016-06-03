package yuan.com.luoling.bean;

import java.io.File;
import java.util.List;

/**
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

    private List<File> list;

    public List<File> getList() {
        return list;
    }

    public void setList(List<File> list) {
        this.list = list;
    }


}
