package yuan.com.luoling;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity管理类
 * Created by YUAN on 2016/10/12.
 */

public class ActivityManagement {
    private static ActivityManagement activityManagement;
    private static List<Activity> activities = new ArrayList<>();

    /**
     * 私有的构造器，避免多次创建对象
     */
    private ActivityManagement() {

    }

    /**
     * 实例化一个对象，创建仅有的一个对象，单例模式
     *
     * @return
     */
    public static ActivityManagement getInstance() {
        if (null == activityManagement) {
            activityManagement = new ActivityManagement();
            ActivityManagement.getInstance().setSendActivity(new ActivityManagement.SendActivity() {
                @Override
                public void createActivity(Activity activity) {
                    ActivityManagement.getInstance().addActivity(activity);
                }

                @Override
                public void destroyActivity(Activity activity) {
                    ActivityManagement.getInstance().removesActivity(activity);
                }
            });
        }
        return activityManagement;
    }

    /**
     * 把每个新创建的activity对象添加到集合中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 把每个销毁的activity重集合中去除
     *
     * @param activity
     */
    public void removesActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 结束当前所有activity
     */
    public void finishAllActivity() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public interface SendActivity {
        void createActivity(Activity activity);

        void destroyActivity(Activity activity);
    }

    public SendActivity sendActivity;

    public void setSendActivity(SendActivity sendActivity) {
        this.sendActivity = sendActivity;
    }
}
