package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.CartGoodsBean
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.bean.ShopCartBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author xyx
 * @date :2019/6/17 14:46
 */
class ShopCartViewModel : BaseViewModel() {

    /**
     * 获取购物车中商品列表
     */
    fun getCarProductList(block: (List<ShopCartBean>) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId() ?: ""
        service.getCarProductList(map).netDispatch(object : RxNetObserver<List<CartGoodsBean>>() {
            override fun onStart() {
            }

            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onSuccess(t: List<CartGoodsBean>?) {
                t?.run {
                    getData(this, block)
                }
            }

        })
    }

    fun getData(list: List<CartGoodsBean>?, block: (List<ShopCartBean>) -> Unit) {
        val goodsList: MutableList<ShopCartBean> = arrayListOf()
        //b是哪个  你给我看的是A
        val hashMapOf = hashMapOf<String?, MutableList<CartGoodsBean>>()

        list?.forEach {
            val merchantName = it.merchantName
            if (hashMapOf[merchantName] == null) {
                hashMapOf[merchantName] = arrayListOf()
            }
            hashMapOf[merchantName]!!.add(it)
        }
        for (mutableEntry in hashMapOf) {
            val shopCartBean = ShopCartBean()
            shopCartBean.merchantName = mutableEntry.key
            shopCartBean.goodsList = mutableEntry.value as ArrayList<CartGoodsBean>
            goodsList.add(shopCartBean)
        }
        block(goodsList)
    }

    /**
     * 获取推荐商品列表
     */
    fun getProductRecList(block: (list: List<ProductRecBean>) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.getCarProductRecList(map).netDispatch(object : RxNetObserver<List<ProductRecBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<ProductRecBean>?) {
                t?.run {
                    block(t)
                }
            }
        })

    }


}