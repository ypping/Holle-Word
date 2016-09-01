package yuan.com.luoling.inter;

/**
 * Created by YUAN on 2016/9/1.
 */
public class UpDataImageIm {
    private static UpDataImageIm upDataImageIm;

    private UpDataImageFile upDataImageFile;

    public static UpDataImageIm getInstance() {
        if (upDataImageIm == null) {
            upDataImageIm = new UpDataImageIm();
        }
        return upDataImageIm;
    }

    public void setUpDataImageFile(UpDataImageFile upDataImageFile) {
        this.upDataImageFile = upDataImageFile;
    }


    public UpDataImageFile getUpDataImageFile() {
        return upDataImageFile;
    }
}
