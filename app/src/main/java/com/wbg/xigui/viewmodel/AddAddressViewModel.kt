package com.wbg.xigui.viewmodel

import android.text.TextUtils
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ParmasAddress
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch
import toast

/**
 * @author tyk
 * @date :2019/8/16 14:44
 * @des :  添加地址viewmodel
 */
class AddAddressViewModel : BaseViewModel() {

    /**
     * 新增地址
     */
    fun addNewOrUpdateAddress(bean: ParmasAddress, successAction: () -> Unit) {
        val map = HashMap<String,Any>()
        if (!TextUtils.isEmpty(bean.addressId)){//地址ID不为空就视为  修改地址 为空就认为是新增地址
            map["addressId"] = bean.addressId
        }
        map["city"] = bean.city
        map["cityCode"] = bean.cityCode
        map["defaultStatus"] = bean.defaultStatus
        map["detailAddress"] = bean.detailAddress
        map["memberId"] = bean.memberId
        map["name"] = bean.name
        map["phoneNumber"] = bean.phoneNumber
        map["postCode"] = bean.postCode
        map["province"] = bean.province
        map["region"] = bean.region
        if (!TextUtils.isEmpty(bean.addressId)){
            service.updateAddress(map).netDispatch(object : RxNetObserver<Any>(true) {
                override fun onError(msg: String) {
                    errorMsg.value = msg
                }

                override fun onStart() {
                }

                override fun onSuccess(t: Any?) {
                    toast("修改成功")
                    successAction()
                }
            })
        }else{
            service.addAddress(map).netDispatch(object : RxNetObserver<Any>(true) {
                override fun onError(msg: String) {
                    errorMsg.value = msg
                }

                override fun onStart() {
                }

                override fun onSuccess(t: Any?) {
                    toast("添加成功")
                    successAction()
                }
            })
        }

    }

}