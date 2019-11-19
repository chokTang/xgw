package com.wbg.xigui.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.CartGoodsBean
import com.wbg.xigui.bean.ShopCartBean

/**
 * @author tyk
 * @date :2019/8/21 16:12
 * @des :
 */

class CartProductAdapter : BaseQuickAdapter<ShopCartBean, BaseViewHolder>(R.layout.shop_cart_first_item) {
    var adapter: CartProductListAdapter? = null
    override fun convert(helper: BaseViewHolder?, item: ShopCartBean?) {

        val linearLayoutManager = LinearLayoutManager(mContext)
        helper?.getView<RecyclerView>(R.id.rv_product)?.layoutManager = linearLayoutManager
        adapter = CartProductListAdapter()
        helper?.getView<RecyclerView>(R.id.rv_product)?.adapter = adapter
        item?.run {
            helper?.setText(R.id.tv_merchant_name, merchantName)
            helper?.getView<ImageView>(R.id.check_img_first)!!.isSelected = isSelected
            if (isSelected) {//title选了 下面的 子项全选
                for (i in 0 until goodsList!!.size) {
                    goodsList!![i].isSelected = true
                }
            } else {//title选了 下面的 子项全选
                for (i in 0 until goodsList!!.size) {
                    goodsList!![i].isSelected = false
                }
            }
            adapter?.setNewData(goodsList)
        }
        helper?.addOnClickListener(R.id.check_img_first)
        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val cartGoodsBean = adapter.data[position] as CartGoodsBean
            when (view.id) {
                R.id.rl_check -> {
                    cartGoodsBean.isSelected = !cartGoodsBean.isSelected
                    adapter.notifyItemChanged(position)
                    //子项点击控制 第一项  只要子项有一个选中就选中第一项
                    var isSelectTitle = false
                    for (i in 0 until adapter.data.size) {
                        val bean = adapter.data[i] as CartGoodsBean
                        if (bean.isSelected) {//如果存在选中 则title选中
                            isSelectTitle = true
                        }
                    }
                    item?.isSelected = isSelectTitle
                    helper?.getView<ImageView>(R.id.check_img_first)!!.isSelected = isSelectTitle
                    clickListener?.refresh()
                }
            }
        }

        adapter?.invoke(object : CartProductListAdapter.ClickListener {
            override fun refresh() {
                clickListener?.refresh()
            }
        })

        adapter?.setOnItemClickListener { _, view, position ->
            clickListener?.ItemClick(view, helper?.adapterPosition!!, position)
        }
    }


    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun ItemClick(v: View?, firstPosition: Int, position: Int)
        fun refresh()
    }

    var clickListener: ClickListener? = null

}