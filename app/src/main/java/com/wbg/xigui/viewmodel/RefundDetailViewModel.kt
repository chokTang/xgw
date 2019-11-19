package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.RefundBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author xyx
 * @date :2019/7/15 14:12
 */
class RefundDetailViewModel : BaseViewModel() {
    /**
     * 获取退款退货详情
     */
    fun getRefundDetail(orderId: String?, skuId: String?, block: (bean: RefundBean?) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        map["skuId"] = skuId!!
        service.getRefundDetail(map).netDispatch(object : RxNetObserver<RefundBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: RefundBean?) {
                block(t)
            }
        })
    }

}