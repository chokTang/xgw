package com.wbg.xigui.utils.tab;

import android.content.Context;
import com.wbg.xigui.R;
import com.wbg.xigui.utils.ResourcesUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

/**
 * <p>
 * 作者：jakee
 * 创建时间：2019/4/15
 */
public class XPagerIndicator extends LinePagerIndicator {
    public XPagerIndicator(Context context) {
        super(context);
        setColors(ResourcesUtil.getColor(R.color.colorAccent));
        setLineHeight(ResourcesUtil.dip2px(3));
        setRoundRadius(ResourcesUtil.dip2px(3));
        setXOffset(-13f);
        setMode(MODE_WRAP_CONTENT);
    }
}
