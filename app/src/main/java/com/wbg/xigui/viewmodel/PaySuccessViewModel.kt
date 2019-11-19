package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author tyk
 * @date :2019/8/21 8:57
 * @des : 商品订单支付成功 viewmodel
 */
class PaySuccessViewModel : BaseViewModel(){
    /**
     * 获取推荐商品列表
     */
    fun getOrderRecList(productId: String,orderId:String, block: (list: List<ProductRecBean>) -> Unit) {
        val map = HashMap<String, Any>()
        map["productId"] = productId
        map["orderId"] = orderId
        map["memberId"] = XApplication.instance.getMemberId()!!
        service.getOrderRecList(map).netDispatch(object : RxNetObserver<List<ProductRecBean>>() {
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