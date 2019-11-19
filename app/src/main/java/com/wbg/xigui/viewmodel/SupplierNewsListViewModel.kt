package com.wbg.xigui.viewmodel

import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.SupplierNewsBean
import com.wbg.xigui.databinding.SupplierNewsImportantItemBinding
import com.wbg.xigui.databinding.SupplierNewsOrderItemBinding
import com.wbg.xigui.databinding.SupplierNewsOtherItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import otherwise
import startRout
import yes
import kotlin.random.Random

/**
 * @author xyx
 * @date :2019/7/16 10:23
 */
class SupplierNewsListViewModel : BaseViewModel() {
    var type = 0
    var list = ObservableReplaceArrayList<SupplierNewsBean>()
    var binding = ItemBinding.of<SupplierNewsBean> { itemBinding, position, item ->
        if (type == 1) {//订单通知
            itemBinding.set(BR.bean, R.layout.supplier_news_order_item)
        } else if (type == 0) {//重要通知
            itemBinding.set(BR.bean, R.layout.supplier_news_important_item)
        } else {//其他通知
            itemBinding.set(BR.bean, R.layout.supplier_news_other_item)
        }
    }
    var adapter = (object : BindingRecyclerViewAdapter<SupplierNewsBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: SupplierNewsBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            when (binding) {
                is SupplierNewsOrderItemBinding -> {//订单通知
                    binding.run {
                        item?.run {
                            stateTv.text = when (status) {
                                -3 -> "已退货"
                                -2 -> "已退款"
                                2 -> "已支付"
                                3 -> "已发货"
                                4 -> "已完成"
                                5 -> "申请退款"
                                6 -> "申请退货"
                                else -> ""
                            }
                            nameTv.text = (type == 1).yes { name }.otherwise { propertyValue }
                        }
                        root.setOnClickListener {
                            Random.nextBoolean().yes { startRout(RoutUrl.Supplier.order_deliver) }
                                .otherwise { startRout(RoutUrl.Supplier.order_refund) }
                        }
                    }
                }
                is SupplierNewsImportantItemBinding -> {//重要通知

                }
                is SupplierNewsOtherItemBinding -> {//其他通知

                }
            }
        }
    })

    fun getData() {
        val map = HashMap<String, Any>()
        map["page"] = mPage
        map["size"] = 20
        service.getSupplierNews(map).netDispatch(object : RxNetObserver<List<SupplierNewsBean>>() {
            override fun onError(msg: String) {
                changeLoadType(list, null, true)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<SupplierNewsBean>?) {
                mPage++
                t?.run {
                    list.addAll(this)
                }
                changeLoadType(list, t, false)
            }
        })
    }

    fun refresh() {
        list.clear()
        mPage = 1
        getData()
    }
}