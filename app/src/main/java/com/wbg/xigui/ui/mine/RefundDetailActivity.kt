package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.PicStringAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.viewmodel.RefundDetailViewModel
import kotlinx.android.synthetic.main.refund_detail_activity.*

/**
 * @author xyx
 * @date :2019/7/15 16:15
 */
@Route(path = RoutUrl.Mine.refund_detail, extras = RoutUrl.Extra.login)
class RefundDetailActivity : BaseXActivity<RefundDetailViewModel>() {

    companion object {
        const val KEY_ORDER_ID = "orde_id"
        const val KEY_SKU_ID = "skuid"
        const val KEY_BUNDLE = "keybundle"

    }

    var adapter: PicStringAdapter? = null
    var orderId = ""
    var skuId = ""

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("退款详情")
        addView(R.layout.refund_detail_activity)

        val bundle = intent.getBundleExtra(KEY_BUNDLE)
        if (bundle!=null){
            skuId = bundle.getString(KEY_SKU_ID)!!
            orderId = bundle.getString(KEY_ORDER_ID)!!
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        mViewModel.getRefundDetail(orderId, skuId) {
            val bean = it
            GlideUtil.loadImg(bean?.thumbnail!!, img_product, this)
            tv_name.text = bean.productName
            tv_des.text = bean.propertyValue
            tv_num.text = "X" + bean.total.toString()
            tv_bood.text = String.format("%.2f", bean?.percentage!! * 100) + "%"
            tv_reason.text = bean.memo
            tv_price.text = "¥" + bean.price

            val linearLayoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_pic.layoutManager = linearLayoutManager
            adapter = PicStringAdapter()
            rv_pic.adapter = adapter
            adapter?.setNewData(bean.images)


            if (bean.status == 0) {
                tv_status.text = "处理中"
            } else {
                tv_status.text = "处理完毕"
            }
            when(bean.status){
                0->{
                    tv_status.text = "退款处理中"
                }
                1->{
                    tv_status.text = "退款处理完毕"
                }
                2->{
                    tv_status.text = "退款已拒绝"

                }
            }
        }


    }
}