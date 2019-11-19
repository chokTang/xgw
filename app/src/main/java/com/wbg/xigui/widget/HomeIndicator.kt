package com.gouwu.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.luck.picture.lib.tools.ScreenUtils

/**
 * 首页指示器
 */
class HomeIndicator(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * 指示器数目
     */
    private var count = 2

    /**
     * 选中颜色
     */
    private val selectColor = Color.parseColor("#fb5175")

    /**
     * 未选中颜色
     */
    private val unselectColor = Color.parseColor("#f5f6fa")

    /**
     * 选中指示器的位置
     */
    private val rectf = RectF()

    /**
     * 指示器高度
     */
    private val indicatorHeight = ScreenUtils.dip2px(context, 8f)

    /**
     * 指示器宽度
     */
    private val indicatorWidth = ScreenUtils.dip2px(context, 8f)

    /**
     * 指示器间隔
     */
    private val indicatorSpace = ScreenUtils.dip2px(context, 8f)

    /**
     * 选中画笔
     */
    private val selectPaint = Paint()

    /**
     * 未选中画笔
     */
    private val unselectPaint = Paint()

    /**
     * 指示器圆角大小
     */
    private var radius = ScreenUtils.dip2px(context, 4f).toFloat()

    init {
        //确定第一个的位置
        rectf.set(0f, 0f, indicatorWidth.toFloat(), indicatorHeight.toFloat())
        //初始化选中画笔
        selectPaint.style = Paint.Style.FILL
        selectPaint.isAntiAlias = true
        selectPaint.color = selectColor
        //初始化未选中画笔
        unselectPaint.style = Paint.Style.FILL
        unselectPaint.isAntiAlias = true
        unselectPaint.color = unselectColor


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = indicatorSpace * (count - 1) + indicatorWidth * count

        setMeasuredDimension(
            width + paddingLeft + paddingRight,
            indicatorHeight + paddingTop + paddingBottom
        )
    }


    /**
     * 在绑定adapter后调用
     */
    fun bindViewPager(viewPager: ViewPager) {
        count = viewPager.adapter!!.count

        requestLayout()
        postInvalidate()

        //滑动绑定
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                //指示器偏移量
                val offset = position + positionOffset

                val left = offset * (indicatorWidth + indicatorSpace)
                val right = left + indicatorWidth

                rectf.set(left, 0f, right, indicatorHeight.toFloat())
                postInvalidate()
            }

            override fun onPageSelected(position: Int) {
            }

        })
    }



    fun setPosition(position: Int){
        //指示器偏移量
        val offset = position.toFloat()

        val left = offset * (indicatorWidth + indicatorSpace)
        val right = left + indicatorWidth

        rectf.set(left, 0f, right, indicatorHeight.toFloat())
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制背景指示器（未选择的）
        drawBg(canvas)
        //绘制前景指示器（选择的）
        drawFront(canvas)

    }

    private fun drawFront(canvas: Canvas?) {
        canvas?.drawRoundRect(
            rectf,
            radius, radius,
            selectPaint
        )
    }

    private fun drawBg(canvas: Canvas?) {
        for (i in 0 until count) {

            val left = i * indicatorWidth + indicatorSpace * i
            val right = (i + 1) * indicatorWidth + indicatorSpace * i
            val top = 0
            val bottom = indicatorHeight

            canvas?.drawRoundRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                radius, radius,
                unselectPaint
            )
        }
    }


}
