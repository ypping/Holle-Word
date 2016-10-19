package yuan.com.luoling.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import yuan.com.luoling.R;
import yuan.com.luoling.bean.ItemPopupWindows;
import yuan.com.luoling.utils.ScreenUtils;

/**
 * @author YUAN
 *         Created by YUAN on 2016/10/14.
 *         标题栏菜单按钮的弹窗
 */
public class MenuPopup extends PopupWindow {
    private Context context;
    /**
     * 列表弹窗的间隔
     */
    private final int LIST_PADDING = 10;
    /**
     * 实例化一个矩形
     */
    private Rect rect = new Rect();
    /**
     * 坐标的位置
     */
    private final int[] mLocation = new int[2];
    /**
     * 屏幕的宽度和高度
     */
    private int mScreenWidth, mScreenHeight;
    /**
     * 判断是否需要添加或更新列表的子列项
     */
    private boolean mIsDirty;
    /**
     * 设置权重位置不在中心
     */
    private int popopGravity = Gravity.NO_GRAVITY;
    /**
     * 子item的点击监听
     */
    private OnItemOnClickListener onItemClickListener;
    /**
     * 控件的布局
     */
    private ListView mListView;
    /**
     * 定义子类项列表
     */
    private ArrayList<ItemPopupWindows> list = new ArrayList<>();

    public MenuPopup(Context context) {
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuPopup(Context context, int width, int height) {
        this.context = context;
        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);

        //获得屏幕的宽度
        mScreenWidth = ScreenUtils.getScreenWidth1(context);
        //获得屏幕的高度
        mScreenHeight = ScreenUtils.getScreenHeight1(context);
        //设置宽度
        setWidth(width);
        //设置高度
        setHeight(height);
        setBackgroundDrawable(new BitmapDrawable());
        setContentView(LayoutInflater.from(context).inflate(R.layout.menu_popup, null));
        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.title_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(position), position);
                }
            }
        });
    }

    /**
     * 显示弹窗列表界面
     *
     * @param view
     */
    public void show(View view) {
        //获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);
        //设置矩形的大小
        Log.d("mLocaton", "mLocation" + mLocation[0] + "mLocation" + mLocation[1]);
        rect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());
        if (mIsDirty) {
            populateItems();
        }
        //显示弹窗的位置
        showAtLocation(view, popopGravity, rect.right, rect.bottom);

    }

    /**
     * 设置弹窗列表子项
     */
    private void populateItems() {
        mIsDirty = false;

        //设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {
                                 @Override
                                 public View getView(int position, View convertView, ViewGroup parent) {
                                     ViewHolder viewHolder;
                                     if (convertView == null) {
                                         viewHolder = new ViewHolder();
                                         convertView = LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, null);
                                         viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
                                         viewHolder.textView.setTextColor(context.getResources().getColor(android.R.color.white));
                                         //设置文本域的范围
                                         viewHolder.textView.setPadding(0, 10, 0, 10);
                                         //设置文本在一行内显示（不换行）
                                         viewHolder.textView.setSingleLine(true);
                                         convertView.setTag(viewHolder);
                                     } else {
                                         viewHolder = (ViewHolder) convertView.getTag();
                                     }
                                     viewHolder.textView.setTextSize(10);
                                     //设置文本居中
                                     //viewHolder.textView.setGravity(Gravity.CENTER);
                                     ItemPopupWindows item = list.get(position);

                                     //设置文本文字
                                     viewHolder.textView.setText(item.getmTitle());
                                     //设置文字与图标的间隔
                                     // textView.setCompoundDrawablePadding(10);
                                     //设置在文字的左边放一个图标
                                     // textView.setCompoundDrawablesWithIntrinsicBounds(item.getmDrawable(), null, null, null);

                                     return convertView;
                                 }

                                 @Override
                                 public long getItemId(int position) {
                                     return position;
                                 }

                                 @Override
                                 public Object getItem(int position) {
                                     return list.get(position);
                                 }

                                 @Override
                                 public int getCount() {
                                     return list.size();
                                 }

                                 class ViewHolder {
                                     TextView textView;
                                 }
                             }
        );

    }

    /**
     * 添加子项
     */
    public void addItem(ItemPopupWindows itemPopupWindows) {
        if (itemPopupWindows != null) {
            list.add(itemPopupWindows);
            mIsDirty = true;
        }
    }

    /**
     * 清除子类项
     */
    public void cleanItem() {
        if (list.isEmpty()) {
            list.clear();
            mIsDirty = true;
        }
    }

    /**
     * 根据位置得到子类项
     *
     * @param position
     * @return
     */
    public ItemPopupWindows getPopupWindows(int position) {
        if (position < 0 || position > list.size()) {
            return null;
        }
        return list.get(position);
    }

    /**
     * 设置监听事件
     *
     * @param onItemClickListener
     */
    public void setItemOnClickListener(OnItemOnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public interface OnItemOnClickListener {
        void onItemClick(ItemPopupWindows itemPopupWindows, int position);
    }
}
