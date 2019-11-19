package com.wbg.xigui.viewmodel

import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.BannerBean
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.bean.StoreTypeBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
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
import kotlin.collections.set

class NearViewModel : BaseViewModel() {
    val bannerList = ObservableReplaceArrayList<BannerBean>()
    val list = ObservableReplaceArrayList<StoreBean>()
    val binding = ItemBinding.of<StoreBean>(BR.bean, R.layout.near_store_item)
    val adapter = (object : BindingRecyclerViewAdapter<StoreBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: StoreBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as com.wbg.xigui.databinding.NearStoreItemBinding).run {
                item?.run {
                    ratingView.rating = score.toFloat()
                    root.setOnClickListener {
                        startRout(
                            RoutUrl.Near.store_detail,
                            KEY_STORE_BEAN,
                            item
                        )
                    }
                    exchangeTv.text =
                        "兑${StrUtil.subZeroAndDot(String.format("%.2f", (bond!! * 100)))}%"
                    salesTv.text = "销量${sale}份"
                }


            }
        }
    })


    fun getData() {
        getStore()
        getBanner()
    }

    /**
     * 获取商家信息
     */
    fun getStore() {
        val map = HashMap<String, Any>()
        val position = XApplication.mLocation
        position?.run {
            map["latitude"] = latitude
            map["longitude"] = longitude
        }
        map["sort "] = 1
        service.getNearStore(map).netDispatch(object : RxNetObserver<List<StoreBean>>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<StoreBean>?) {
                t?.run {
                    list.replaceAll(this)
                }
            }
        })
    }

    fun goType() {
        startRout(RoutUrl.Near.type_store)
    }

    fun goSearch() {
        startRout(RoutUrl.Common.search)
    }

    fun getBanner() {
        val map = HashMap<String, Any>()
        map["location"] = 2
        service.getBanner(map.getParam()).netDispatch(object : RxNetObserver<List<BannerBean>>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<BannerBean>?) {
                t?.run {
                    bannerList.replaceAll(this)
                }

            }
        })
    }

    /**
     * 获取模块列表
     */
    fun getType(block: (t: List<StoreTypeBean>?) -> Unit) {
        service.getStoreType(HashMap()).netDispatch(object : RxNetObserver<List<StoreTypeBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<StoreTypeBean>?) {
                block(t)
            }
        })
    }




}