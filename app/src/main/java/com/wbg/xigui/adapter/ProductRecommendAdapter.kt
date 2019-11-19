package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/15 9:20
 * @des : 推荐商品适配器
 */

class ProductRecommendAdapter : BaseQuickAdapter<ProductRecBean, BaseViewHolder>(R.layout.item_recommend_product) {
    override fun convert(helper: BaseViewHolder?, item: ProductRecBean?) {
        item?.run {
            GlideUtil.loadImg(thumbnail!!,helper!!.getView<ImageView>(R.id.img_product),mContext)
            helper.setText(R.id.tv_product_name,name)
            helper.setText(R.id.tv_price,"￥$price")
            helper.addOnClickListener(R.id.rl_add_car)
        }
    }
}