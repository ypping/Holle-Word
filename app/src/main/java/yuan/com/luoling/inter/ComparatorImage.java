package yuan.com.luoling.inter;

import java.util.Comparator;

import yuan.com.luoling.bean.ImageFile;

/**
 * Created by YUAN on 2016/9/1.
 */
public class ComparatorImage implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        ImageFile image = (ImageFile) lhs;
        ImageFile imageFile = (ImageFile) rhs;

        //首先比较大小，如果年龄相同，则比较名字

        int flag = image.getSize().compareTo(imageFile.getSize());
        if (flag == 0) {
            return image.getName().compareTo(imageFile.getName());
        } else {
            return flag;
        }
    }
}
