package com.wbg.xigui.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.bean.StoreTypeBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.near.StoreDetailActivity.Companion.KEY_STORE_BEAN
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import startRout
import toast

/**
 * @author xyx
 * @date :2019/6/18 16:51
 */
class StoreForTypeViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<StoreBean>()
    var currentType = MutableLiveData<StoreTypeBean>()
    var sort = 1
    val binding = ItemBinding.of<StoreBean>(BR.bean, R.layout.store_for_type_item)
    val adapter = (object : BindingRecyclerViewAdapter<StoreBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: StoreBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as com.wbg.xigui.databinding.StoreForTypeItemBinding).run {
                item?.run {
                    ratingView.rating = score.toFloat()
                    root.setOnClickListener { startRout(RoutUrl.Near.store_detail, KEY_STORE_BEAN, item) }
                    exchangeTv.text = "兑${StrUtil.subZeroAndDot(String.format("%.2f", (bond!! * 100)))}%"
                    salesTv.text = "销量${sale}份"
                }
            }
        }
    })

    fun getData() {
        val map = HashMap<String, Any>()
        var position = XApplication.mLocation
        position?.run {
            map["latitude"] = latitude
            map["longitude"] = longitude
        }
        map["category"] = currentType.value?.id ?: ""
        map["page"] = mPage
        map["size"] = 20
        map["sort"] = sort
        service.getNearStore(map).netDispatch(object : RxNetObserver<List<StoreBean>>() {
            override fun onError(msg: String) {
                toast(msg)
                changeLoadType(list, null, true)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<StoreBean>?) {
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