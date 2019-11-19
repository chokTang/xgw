package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.RefundBean
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/23 10:49
 * @des :  退款适配器
 */

class ReFundAdapter :
    BaseQuickAdapter<RefundBean, BaseViewHolder>(R.layout.refund_goods_item) {


    override fun convert(helper: BaseViewHolder?, item: RefundBean?) {
        item?.run {
            helper!!.setText(R.id.tv_name, productName)
            helper.setText(R.id.tv_des, propertyValue)
            when(status){
                0->{
                    helper.setText(R.id.tv_progress, "退款处理中")
                }
                1->{
                    helper.setText(R.id.tv_progress, "退款处理完毕")
                }
                2->{
                    helper.setText(R.id.tv_progress, "退款已拒绝")
                }
            }
            helper.setText(R.id.tv_price, "¥$price")
            helper.setText(R.id.tv_num, "X$total")
            helper.setText(R.id.tv_bood, String.format("%.2f", percentage!! * 100) + "%")
            GlideUtil.loadImg(thumbnail!!, helper?.getView<ImageView>(R.id.img_product)!!, mContext)
        }
    }


}