package com.wbg.xigui.viewmodel

import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author xyx
 * @date :2019/7/16 14:47
 */
class SupplierOrderRefundViewModel : BaseViewModel() {
    var list = ObservableReplaceArrayList<String>()
    var binding = ItemBinding.of<String>(BR.url, R.layout.refund_pic_item)
    var adapter = (object : BindingRecyclerViewAdapter<String>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: String?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
        }
    })

    fun getData() {

    }

}