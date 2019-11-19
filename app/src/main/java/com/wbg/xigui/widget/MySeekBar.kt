package com.wbg.xigui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.wbg.xigui.R

/**
 * Created by ${liuyu} 2019/7/17 15:13
 */
class MySeekBar : LinearLayout, SeekBar.OnSeekBarChangeListener {

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.text_progress, this)
        initView(this)
    }


    private lateinit var startTv: TextView
    private lateinit var endTv: TextView
    private lateinit var seekbar: SeekBar
    private lateinit var leftView: View
    private lateinit var rightView: View
    private lateinit var progressTv: TextView
    private var mIsListening = true

    private fun initView(myProgressBar: MySeekBar) {
        startTv = myProgressBar.findViewById(R.id.startTv)
        endTv = myProgressBar.findViewById(R.id.endTv)
        seekbar = myProgressBar.findViewById(R.id.seekbar)
        leftView = myProgressBar.findViewById(R.id.leftView)
        rightView = myProgressBar.findViewById(R.id.rightView)
        progressTv = myProgressBar.findViewById(R.id.progressTv)

        seekbar.setOnSeekBarChangeListener(this)
    }


    fun setProgress(progress: Int) {
        seekbar.progress = progress
    }

    /**
     * 设置下方左边textView内容
     */
    fun setSeekbarText(str: String) {
        progressTv.text = str
    }

    /**
     * 设置上方左边textView内容
     */
    fun setStartText(str: String) {
        startTv.text = str
    }

    /**
     * 设置上方右边textView内容
     */
    fun setEndText(str: String) {
        endTv.text = str
    }

    /**
     * 设置上方右边textView颜色
     */
    fun setEndTextColor(color: Int) {
        endTv.setTextColor(color)
    }

    /**
     * 设置上方左边textView颜色
     */
    fun setStartTextColor(color: Int) {
        startTv.setTextColor(color)
    }

    /**
     * 设置下方textView颜色
     */
    fun setSeekbarTextColor(color: Int) {
        progressTv.setTextColor(color)
    }

    /**
     * 设置上方右边textView大小
     */
    fun setEndTextSize(size: Float) {
        endTv.textSize = size
    }

    /**
     * 设置上方左边textView颜色
     */
    fun setStartTextSize(size: Float) {
        startTv.textSize = size
    }

    /**
     * 设置下方textView颜色
     */
    fun setSeekbarTextSize(size: Float) {
        progressTv.textSize = size
    }

    /**
     * 设置是否根据进度条值改变下方textView内容
     */
    fun isListeningChanged(isListening: Boolean) {
        mIsListening = isListening
    }

    /**
     * 设置进度条drawable
     */
    fun setSeekbarDrawable(drawable: Drawable) {
        seekbar.progressDrawable = drawable
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // getTextView01设置权重是1
        val lp = LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, progress.toFloat())
        leftView.layoutParams = lp
        // getTextView02设置权重是2
        val lp1 = LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, (seekbar.max - progress).toFloat())
        rightView.layoutParams = lp1

        if (mIsListening) {
            setSeekbarText(progress.toString())
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}