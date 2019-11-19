package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ListBean
import com.wbg.xigui.bean.PaymentDetailBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author xyx
 * @date :2019/7/16 15:18
 */
class SupplierPaymentDetailViewModel : BaseViewModel() {
    val list = MutableLiveData<List<PaymentDetailBean>>()
    fun getData(date: String) {
        val map = HashMap<String, Any>()
        map["time"] = date
        service.getIncome(map).netDispatch(object : RxNetObserver<ListBean<PaymentDetailBean>>(true) {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: ListBean<PaymentDetailBean>?) {
                t?.run {
                    this@SupplierPaymentDetailViewModel.list.value = list
                }
            }
        })
    }
}