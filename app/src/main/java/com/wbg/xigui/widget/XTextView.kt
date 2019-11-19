package com.wbg.xigui.widget

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import com.wbg.xigui.R
import com.wbg.xigui.utils.ResourcesUtil

/**
 * @author xyx
 * @date :2019/6/26 11:22
 */
class XTextView : TextView {
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        var builder = SpannableStringBuilder()
        var suffixSpannableString = SpannableString("$text*")

        //设置后缀文字颜色
        var colorSpan = ForegroundColorSpan(ResourcesUtil.getColor(R.color.theme))
        suffixSpannableString.setSpan(colorSpan, text.length, text.length + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        //添加到builder
        builder.append(suffixSpannableString)
        text = builder
        builder.clear()
    }

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)


}