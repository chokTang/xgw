package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.AddressBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author tyk
 * @date :2019/8/16 10:40
 * @des : 地址列表viewmodel
 */
class AddressViewModel : BaseViewModel() {

    /**
     * 获取地址列表
     */
    fun getAddressList(block: (AddressBean) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId() ?: ""
        map["page"] = 1
        map["size"] = 20
        service.getAddressList(map).netDispatch(object : RxNetObserver<AddressBean>() {
            override fun onStart() {
            }

            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onSuccess(t: AddressBean?) {
                t?.run {
                    block(this)
                }
            }

        })
    }


    /**
     * 删除地址
     */
    fun deleteAddress(addressId: String, successAction: () -> Unit) {
        val map = HashMap<String, Any>()
        map["addressId"] = addressId
        service.deleteAddress(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onStart() {
            }

            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onSuccess(t: Any?) {
                t?.run {
                    successAction()
                }
            }

        })
    }


}