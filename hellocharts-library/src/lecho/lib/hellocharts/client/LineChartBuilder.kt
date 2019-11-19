package lecho.lib.hellocharts.client

import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView

/**
 * Created by ${liuyu} 2019/7/16 10:59
 */
object LineChartBuilder {

    private var xList: ArrayList<String>? = null
    private var yList: ArrayList<Float>? = null
    private var chart: LineChartView? = null
    private var color: Int = ChartUtils.DEFAULT_COLOR
    private var lineWidth: Float = 3f
    private var pointRadius: Int = 6
    private var pointColor: Int = ChartUtils.DEFAULT_COLOR
    private var hasXLine: Boolean = false
    private var hasYLine: Boolean = true
    private var isTouchEnable: Boolean = false

    /**
     * 设置X轴数据
     */
    fun setXListData(xListData: ArrayList<String>): LineChartBuilder {
        this.xList = xListData
        return this
    }

    /**
     * 设置Y轴数据
     */
    fun setYListData(yListData: ArrayList<Float>): LineChartBuilder {
        this.yList = yListData
        return this
    }

    /**
     * 设置折线图View
     */
    fun createLineChart(chart: LineChartView): LineChartBuilder {
        this.chart = chart
        return this
    }

    /**
     * 设置表颜色(点,线,填充统一使用该颜色)
     */
    fun setColor(color: Int): LineChartBuilder {
        this.color = color
        if (pointColor == ChartUtils.DEFAULT_COLOR)
            pointColor = color
        return this
    }

    /**
     * 设置折线宽度
     */
    fun setLineWidth(width: Float): LineChartBuilder {
        this.lineWidth = width
        return this
    }

    /**
     * 设置圆点大小
     */
    fun setPointRadius(radius: Int): LineChartBuilder {
        this.pointRadius = radius
        return this
    }

    /**
     * 设置圆点颜色
     */
    fun setPointColor(pointColor: Int): LineChartBuilder {
        this.pointColor = pointColor
        return this
    }

    /**
     * 设置是否显示横轴
     */
    fun setHasXLine(isHas: Boolean): LineChartBuilder {
        this.hasXLine = isHas
        return this
    }

    /**
     * 设置是否显示纵轴
     */
    fun setHasYLine(isHas: Boolean): LineChartBuilder {
        this.hasYLine = isHas
        return this
    }

    /**
     * 设置图标是否可点击放大
     */
    fun setTouchEnable(isTouchEnable: Boolean): LineChartBuilder {
        this.isTouchEnable = isTouchEnable
        return this
    }


    fun build(): LineChartClient {
        return LineChartClient(
            xList, yList, chart, lineWidth, pointColor, pointRadius, color, hasXLine, hasYLine,
            isTouchEnable
        )
    }
}