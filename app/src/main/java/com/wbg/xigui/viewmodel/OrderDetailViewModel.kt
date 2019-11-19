package com.wbg.xigui.viewmodel

import android.text.TextUtils
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.FlagBean
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author xyx
 * @date :2019/7/8 14:40
 */
class OrderDetailViewModel : BaseViewModel() {

    /**
     * 获取订单详情
     */
    fun getOrderDetail(orderId: String?, skuId: String?, block: (bean: OrderBean?) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        map["skuId"] = skuId!!
        service.getOrderDetail(map).netDispatch(object : RxNetObserver<OrderBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: OrderBean?) {
                block(t)
            }
        })
    }


    /**
     * 获取推荐商品列表
     */
    fun getProductRecList(productId: String, skuId: String, block: (list: List<ProductRecBean>) -> Unit) {
        val map = HashMap<String, Any>()
        map["productId"] = productId
        if (!TextUtils.isEmpty(skuId)) {
            map["skuId"] = skuId
        }
        service.getProductRecList(map).netDispatch(object : RxNetObserver<List<ProductRecBean>>() {
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



    /**
     * 删除订单
     */
    fun deleteOrder(orderId:String?,block:(bean: FlagBean?)->Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        service.deleteOrder(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }


    /**
     * 完成订单
     */
    fun completeOrder(orderId:String?,skuId:String?,block:(bean: FlagBean?)->Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        map["skuId"] =skuId!!
        service.completeOrder(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }
    /**
     * 取消订单
     */
    fun cancelOrder(orderId:String?,block:(bean: FlagBean?)->Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        service.cancelOrder(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }

}