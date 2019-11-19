package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.ProductDetailBean
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/19 16:29
 * @des : 订单中商店 下面的商品列表适配器
 */
class ProductAdapter :
    BaseQuickAdapter<ProductDetailBean, BaseViewHolder>(R.layout.item_order_shop_product) {
    override fun convert(helper: BaseViewHolder?, item: ProductDetailBean?) {
        item?.run {
            GlideUtil.loadImg(thumbnail!!, helper?.getView<ImageView>(R.id.img_product)!!, mContext)
            helper.setText(R.id.tv_name, name)
            helper.setText(R.id.tv_des, "$des X$num")
            helper.setText(R.id.tv_price, "¥$orderPrice")
            helper.setText(R.id.tv_coupon, bond)
        }

    }
}