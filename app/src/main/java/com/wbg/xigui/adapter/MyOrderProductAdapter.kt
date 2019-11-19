package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.wbg.xigui.R
import com.wbg.xigui.bean.IndentProductSkuView
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/23 10:49
 * @des :  我的订单中 商品适配器
 */

class MyOrderProductAdapter : BaseQuickAdapter<IndentProductSkuView, BaseViewHolder>(R.layout.item_my_order_product) {

    companion object{
        const val TYPE_REFOUND_ORDER = 1//商品退货单
        const val TYPE_ORDER = 2//商品正常订单
    }

    init {
        multiTypeDelegate = object : MultiTypeDelegate<IndentProductSkuView>() {
            override fun getItemType(item: IndentProductSkuView?): Int {
//                return when (item?.status) {
//                    -3, -2, 5, 6 -> {
//                        TYPE_REFOUND_ORDER
//                    }
//                    else -> {
//                        TYPE_ORDER
//                    }
//                }

                return TYPE_ORDER
            }
        }
        multiTypeDelegate.registerItemType(TYPE_ORDER,R.layout.item_my_order_product)
            .registerItemType(TYPE_REFOUND_ORDER, R.layout.refund_goods_item)

    }

    override fun convert(helper: BaseViewHolder?, item: IndentProductSkuView?) {
        when(helper?.itemViewType){
            TYPE_ORDER->{//商品订单
                item?.run {
                    helper.setText(R.id.tv_name, name)
                    helper.setText(R.id.tv_des, propertyValue)
                    helper.setText(R.id.tv_price, "¥$price")
                    helper.setText(R.id.tv_num, "X$total")
                    helper.setText(R.id.tv_bood, String.format("%.2f", bondPortion * 100) + "%")
                    GlideUtil.loadImg(image!!, helper?.getView<ImageView>(R.id.img_product)!!, mContext)
                }
            }

            TYPE_REFOUND_ORDER->{//商品退货单

            }
        }


    }
}