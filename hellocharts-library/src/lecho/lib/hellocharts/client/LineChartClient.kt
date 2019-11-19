package lecho.lib.hellocharts.client

import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView

/**
 * Created by ${liuyu} 2019/7/16 10:26
 */
class LineChartClient(
    private var xList: ArrayList<String>?,
    private var yList: ArrayList<Float>?,
    private var chart: LineChartView?,
    private var lineWidth: Float,
    private var pointColor: Int,
    private var PointRadius: Int,
    private var color: Int,
    private var hasXLine: Boolean,
    private var hasYLine: Boolean,
    private var isTouchEnable: Boolean
) {


    /**
     * 设置线图数据
     */
    fun create() {
        val values = arrayListOf<PointValue>()
        val axisValues = arrayListOf<AxisValue>()

        if (!xList.isNullOrEmpty() && !yList.isNullOrEmpty() && chart != null) {
            repeat(xList!!.size) {
                values.add(PointValue(it.toFloat(), yList!![it]))
                axisValues.add(AxisValue(it.toFloat()).setLabel(xList!![it]))
            }
            val line = Line(values).setColor(color)
                .setShape(ValueShape.CIRCLE)
                .setStrokeWidth(lineWidth)
                .setPointColor(pointColor)
                .setPointRadius(PointRadius)
                .setFilled(true)
                .setHasGradientToTransparent(true)
            val lines = arrayListOf<Line>()
            lines.add(line)
            val axisY = Axis().setHasLines(hasYLine)
            val data = LineChartData()
            data.axisXBottom = Axis(axisValues).setHasLines(hasXLine)
            data.axisYLeft = axisY
            data.lines = lines
            chart?.lineChartData = data
            chart?.isValueTouchEnabled = isTouchEnable

            resetViewport(values.size.toFloat())
        }
    }

    /**
     * 设置折线图的边距
     */
    private fun resetViewport(points: Float) {
        // Reset viewport height range to (0,100)
        val v = Viewport(chart?.maximumViewport)
        v.bottom = 0f
        v.top = yList?.max()?:0f+5
        v.left = 0f
        v.right = points-0.5f
        chart?.maximumViewport = v
        chart?.currentViewport = v
    }

    fun setData(xList: ArrayList<String>?,yList: ArrayList<Float>?) {
        val values = arrayListOf<PointValue>()
        val axisValues = arrayListOf<AxisValue>()
        if (xList.isNullOrEmpty() || yList.isNullOrEmpty() || chart == null) {
            return
        }
        repeat(xList!!.size) {
            values.add(PointValue(it.toFloat(), yList!![it]))
            axisValues.add(AxisValue(it.toFloat()).setLabel(xList!![it]))
        }
        val line = Line(values).setColor(color)
            .setShape(ValueShape.CIRCLE)
            .setStrokeWidth(lineWidth)
            .setPointColor(pointColor)
            .setPointRadius(PointRadius)
            .setFilled(true)
            .setHasGradientToTransparent(true)
        val lines = arrayListOf<Line>()
        lines.add(line)
        val axisY = Axis().setHasLines(hasYLine)
        val data = LineChartData()
        data.axisXBottom = Axis(axisValues).setHasLines(hasXLine)
        data.axisYLeft = axisY
        data.lines = lines
        chart?.lineChartData = data
        chart?.isValueTouchEnabled = isTouchEnable
        resetViewport(values.size.toFloat())
    }
}