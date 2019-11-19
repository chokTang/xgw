package com.wbg.xigui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.widget.NestedScrollView;

/**
 * <p>
 * 作者：jakee
 * 创建时间：2018/7/27
 */
public class MyScrollView extends NestedScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;
    public ScrollViewListener scrollViewListener = null;
    private View listView;

    public MyScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (listView != null && checkArea(listView, e)) {//listview触摸范围内拦截父控件接收event
            return false;
        }
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    public void setOnScrollListtener(ScrollViewListener s) {
        scrollViewListener = s;
    }

    /**
     * 测试view是否在点击范围内
     *
     * @param v
     * @return
     */
    private boolean checkArea(View v, MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        int[] locate = new int[2];
        v.getLocationOnScreen(locate);
        int l = locate[0];
        int r = l + v.getWidth();
        int t = locate[1];
        int b = t + v.getHeight();
        if (l < x && x < r && t < y && y < b) {
            return true;
        }
        return false;
    }

    public void setListView(View listView) {
        this.listView = listView;
    }

}
