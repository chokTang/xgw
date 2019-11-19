package com.wbg.xigui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.wbg.xigui.R
import com.wbg.xigui.bean.SearchProduct
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/29 19:31
 * @des : 搜索商品结果适配器
 */
class SearchProducAdapter : BaseQuickAdapter<SearchProduct, BaseViewHolder>(R.layout.search_goods_item) {

    override fun convert(helper: BaseViewHolder?, item: SearchProduct?) {
        item?.run {
            GlideUtil.loadImg(thumbnail!!, helper?.getView<RoundedImageView>(R.id.img_product)!!, mContext)
            helper.setText(R.id.tv_name, productName)
            helper.setText(R.id.tv_now_price, "￥$price")
            helper.setText(R.id.tv_bood, "兑$percentage")
            helper.setText(R.id.tv_sale, "已售${sale}件")
        }
    }
}