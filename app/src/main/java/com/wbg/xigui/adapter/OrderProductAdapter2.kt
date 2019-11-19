package com.wbg.xigui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.ShopCartBean

/**
 * @author tyk
 * @date :2019/8/19 15:59
 * @des : 生成订单中 的商品适配器
 */

class OrderProductAdapter2 : BaseQuickAdapter<ShopCartBean, BaseViewHolder>(R.layout.item_order_product) {
    var adapter: ProductAdapter2? = null

    override fun convert(helper: BaseViewHolder?, item: ShopCartBean?) {
        helper?.setText(R.id.tv_shop_name,item?.merchantName)
        val linearLayoutManager = LinearLayoutManager(mContext)
        helper?.getView<RecyclerView>(R.id.rv_shop_product)?.layoutManager = linearLayoutManager
        adapter = ProductAdapter2()
        helper?.getView<RecyclerView>(R.id.rv_shop_product)?.adapter = adapter
        adapter?.setNewData(item?.goodsList)

    }
}