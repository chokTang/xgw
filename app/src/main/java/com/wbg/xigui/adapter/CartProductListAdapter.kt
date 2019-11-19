package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.CartGoodsBean
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.widget.AddView
import log

/**
 * @author tyk
 * @date :2019/8/21 16:17
 * @des : 购物车  每个商店下面的商品列表适配器
 */

class CartProductListAdapter : BaseQuickAdapter<CartGoodsBean, BaseViewHolder>(R.layout.shop_cart_second_item) {
    override fun convert(helper: BaseViewHolder?, item: CartGoodsBean?) {
        helper?.getView<AddView>(R.id.add_view)?.setNumChangeListener(object : AddView.NumChangedListener {
            override fun onNumChange(int: Int) {
                item?.total = int
                changeNum(int) { helper.getView<AddView>(R.id.add_view)?.setNum(int) }
                clickListener?.refresh()
            }
        })
        item?.run {
            helper!!.setText(R.id.tv_name,name)
            helper.getView<AddView>(R.id.add_view)?.setNum(item.total!!)
            helper.setText(R.id.tv_price,"¥"+String.format("%.2f",price))
            helper.setText(R.id.tv_coupon,"兑"+String.format("%.2f", bond!!*100)+"%")
            GlideUtil.loadImg(thumbnail!!, helper.getView(R.id.img_product), mContext)
            helper.getView<ImageView>(R.id.check_img).isSelected = isSelected
        }
        helper?.addOnClickListener(R.id.rl_check)

    }

    fun changeNum(num: Int, block: () -> Unit) {
        if (num > 3) {
            log("$num")
        }
        block()
    }

    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun refresh()
    }

    var clickListener: ClickListener? = null

}