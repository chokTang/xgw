package com.wbg.xigui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar

/**
 * @author xyx
 * @date :2019/7/17 17:03
 */
class XSeekBar : SeekBar {
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        //原来是要将TouchEvent传递下去的,我们不让它传递下去就行了
        //return super.onTouchEvent(event);

        return false
    }
}