package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hard.imageratingview.ImageRatingView
import com.wbg.xigui.R
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.StrUtil

/**
 * @author tyk
 * @date :2019/8/30 9:33
 * @des :
 */
class MerchantAdapter : BaseQuickAdapter<StoreBean, BaseViewHolder>(R.layout.item_merchant) {

    override fun convert(helper: BaseViewHolder?, item: StoreBean?) {
        item?.run {
            GlideUtil.loadImg(thumbnail!!,helper?.getView<ImageView>(R.id.img_product)!!,mContext)
            helper.getView<ImageRatingView>(R.id.rating_view).rating = score
            helper.setText(R.id.tv_merchant_name,name)
            helper.setText(R.id.tv_distance,distance)
            helper.setText(R.id.tv_address,address)
            helper.setText(R.id.sales_tv,"销量${sale}份")
            helper.setText(R.id.exchange_tv,"兑${StrUtil.subZeroAndDot(String.format("%.2f", (bond!! * 100)))}%")
        }

    }

}