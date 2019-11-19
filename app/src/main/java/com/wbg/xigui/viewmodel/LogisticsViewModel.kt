package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author tyk
 * @date :2019/7/15 16:40
 */
class LogisticsViewModel : BaseViewModel() {




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


}