package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.SupplierGoodsBean
import com.wbg.xigui.databinding.SupplierGoodsItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch

/**
 * @author xyx
 * @date :2019/7/16 11:14
 */
class SupplierGoodsViewModel : BaseViewModel() {
    var list = ObservableReplaceArrayList<SupplierGoodsBean>()
    var binding = ItemBinding.of<SupplierGoodsBean>(BR.bean, R.layout.supplier_goods_item)
    var adapter = (object : BindingRecyclerViewAdapter<SupplierGoodsBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: SupplierGoodsBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as SupplierGoodsItemBinding).run {
                item?.run {
                    stateTv.text = when (itemUpshelf) {
                        0 -> "已下架"
                        1 -> "销售中"
                        2 -> "线下商品"
                        else -> ""
                    }
                    if (position != 0 && list[position - 1].itemUpshelf == itemUpshelf) {
                        stateTv.visibility = View.GONE
                    } else {
                        stateTv.visibility = View.VISIBLE
                    }
                    nameTv.text = when (itemUpshelf) {
                        2 -> "线下商品"
                        else -> "${name + propertyValue}"
                    }
                    priceTv.text = when (itemUpshelf) {
                        2 -> "兑换比例$price"
                        else -> "￥$price"
                    }
                    salesNumTv.text = when (itemUpshelf) {
                        2 -> ""
                        else -> "已售${number}件"
                    }
                    stockTv.text = when (itemUpshelf) {
                        2 -> ""
                        else -> "剩余库存${stock}件"
                    }
                }
            }
        }
    })

    fun refresh() {
//        var mlist = arrayListOf<Any>(Any(), Any(), Any(), Any(), Any(), Any(), Any())
//        list.replaceAll(mlist)
//        changeLoadType(list, mlist, false)
        service.getSupplierGoods(HashMap()).netDispatch(object : RxNetObserver<List<SupplierGoodsBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
                changeLoadType(list, null, true)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<SupplierGoodsBean>?) {
                t?.run {
                    list.replaceAll(this)
                }
                changeLoadType(list, t, false)
            }
        })
    }
}