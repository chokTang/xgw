package com.wbg.xigui.utils.tab;

import android.content.Context;
import com.wbg.xigui.R;
import com.wbg.xigui.utils.ResourcesUtil;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * <p>
 * 作者：jakee
 * 创建时间：2019/4/15
 */
public class XPagerTitleView extends SimplePagerTitleView {
    protected int mSelectedSize = 18;
    protected int mNormalSize = 16;

    public int getmSelectedSize() {
        return mSelectedSize;
    }

    public void setmSelectedSize(int mSelectedSize) {
        this.mSelectedSize = mSelectedSize;
    }

    public int getmNormalSize() {
        return mNormalSize;
    }

    public void setmNormalSize(int mNormalSize) {
        this.mNormalSize = mNormalSize;
    }

    public XPagerTitleView(Context context) {
        super(context);
        setSelectedColor(ResourcesUtil.getColor(R.color.theme));
        setNormalColor(ResourcesUtil.getColor(R.color.text_gray));
        int padding= UIUtil.dip2px(context, 30);
        setPadding(padding, 0, padding, 0);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        super.onSelected(index, totalCount);
        setTextSize(mSelectedSize);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        super.onDeselected(index, totalCount);
        setTextSize(mNormalSize);
    }
}
