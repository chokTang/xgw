package com.wbg.xigui.ui.supplier

import android.app.DatePickerDialog
import android.widget.DatePicker
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.SupplierPaymentDetailViewModel
import kotlinx.android.synthetic.main.supplier_payment_detail.*
import lecho.lib.hellocharts.client.LineChartBuilder
import otherwise
import yes
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author xyx
 * @date :2019/7/16 15:18
 */
@Route(path = RoutUrl.Supplier.payment_detail)
class SupplierPaymentDetailActivity : BaseXActivity<SupplierPaymentDetailViewModel>() {
    var date = ""
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("货款明细")
        addView(R.layout.supplier_payment_detail)
    }

    override fun initData() {
//        val x = arrayListOf("01-02", "01-03", "01-04", "01-05", "01-06", "01-07")
//        val y = arrayListOf(300f, 450f, 210f, 600f, 400f, 60f)
//        val x0 = arrayListOf("01-02", "01-03", "01-04", "01-05", "01-06", "01-07")
//        val y0 = arrayListOf(600f, 400f, 300f, 450f, 210f, 60f)
        val line = LineChartBuilder.createLineChart(chart)
            .setColor(ResourcesUtil.getColor(R.color.theme))
            .setLineWidth(1.5f)
            .setPointRadius(4)
//            .setXListData(x)
//            .setYListData(y)
            .build()
        line.create()
        mViewModel.list.observe(this, androidx.lifecycle.Observer {
            val x = ArrayList<String>()
            val y = ArrayList<Float>()
            it.forEach { bean ->
                x.add(bean.incomeTime?.substring(5) ?: "")
                y.add(bean.totalAmt?.toFloat()?:0.0f)
            }
            it.last().run {
                income_num_tv.text = "总收入（共${totalNumber}笔）"
                money_tv.text = "￥$totalAmt"
            }
            line.setData(x, y)
        })
        val calender = Calendar.getInstance()
        calender.add(Calendar.DATE, -1)
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        date_tv.text =
            "${year}年${((month + 1) < 10).yes { "0" + (month + 1) }.otherwise { month + 1 }}月${(day < 10).yes { "0$day" }.otherwise { day }}日"
        mViewModel.getData("$year-${((month + 1) < 10).yes { "0" + (month + 1) }.otherwise { month + 1 }}-${(day < 10).yes { "0$day" }.otherwise { date }}")
        date_tv.setOnClickListener {
            val pickDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    date_tv.text =
                        "${year}年${((month + 1) < 10).yes { "0" + (month + 1) }.otherwise { month + 1 }}月${(dayOfMonth < 10).yes { "0$dayOfMonth" }.otherwise { dayOfMonth }}日"
                    date =
                        "$year-${((month + 1) < 10).yes { "0" + (month + 1) }.otherwise { month + 1 }}-${(dayOfMonth < 10).yes { "0$dayOfMonth" }.otherwise { dayOfMonth }}"
//                    Random.nextBoolean().yes { line.setData(x, y) }.otherwise { line.setData(x0, y0) }
                    mViewModel.getData(date)
                }

            }, year, month, day)
            pickDialog.datePicker.maxDate = calender.time.time
            pickDialog.show()
        }
    }
}