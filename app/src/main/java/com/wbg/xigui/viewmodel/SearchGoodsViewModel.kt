package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.SearchProduct
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.widget.ObservableReplaceArrayList
import netDispatch
import toast

/**
 * @author xyx
 * @date :2019/6/21 10:56
 */
class SearchGoodsViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<SearchProduct>()
    val listMerchant = ObservableReplaceArrayList<StoreBean>()

    /**
     * 获取搜索商店列表
     */
    fun getDataMerchant(hotWords: String?, sort: Int, block: (List<StoreBean>) -> Unit) {
        val map = HashMap<String, Any>()
        val position = XApplication.mLocation
        position?.run {
            map["latitude"] = latitude
            map["longitude"] = longitude
        }
        map["sort"] = sort
        map["hotWords"] = hotWords!!
        map["page"] = mPage
        map["size"] = 20
        service.searchMerchantList(map).netDispatch(object : RxNetObserver<List<StoreBean>>() {
            override fun onError(msg: String) {
                toast(msg)
                changeLoadType(list, null, true)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<StoreBean>?) {
                mPage++
                t?.run {
                    listMerchant.addAll(this)
                    block(listMerchant)
                }
                changeLoadType(listMerchant, t, false)
                if (listMerchant.size == 0) {
                    block(emptyList())
                }
            }
        })
    }

    fun refreshMechant(word: String, type: Int, block: (List<StoreBean>) -> Unit) {
        mPage = 1
        listMerchant.clear()
        getDataMerchant(word, type, block)
    }


    /**
     * 获取搜索商品列表
     */
    fun getData(hotWords: String?, sort: Int, block: (List<SearchProduct>) -> Unit) {
        val map = HashMap<String, Any>()
        val position = XApplication.mLocation
        position?.run {
            map["latitude"] = latitude
            map["longitude"] = longitude
        }
        map["sort"] = sort
        map["hotWords"] = hotWords!!
        map["page"] = mPage
        map["size"] = 20
        service.searchProductList(map).netDispatch(object : RxNetObserver<List<SearchProduct>>() {
            override fun onError(msg: String) {
                toast(msg)
                changeLoadType(list, null, true)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<SearchProduct>?) {
                mPage++
                t?.run {
                    list.addAll(this)
                    block(list)
                }
                changeLoadType(list, t, false)
                if (list.size == 0) {
                    block(emptyList())
                }
            }
        })
    }

    fun refresh(word: String, type: Int, block: (List<SearchProduct>) -> Unit) {
        mPage = 1
        list.clear()
        getData(word, type, block)
    }
}