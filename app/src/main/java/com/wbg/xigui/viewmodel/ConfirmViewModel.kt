package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ConfirmBean
import com.wbg.xigui.bean.ConfirmSuccessBean
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import netDispatch
import toast

/**
 * @author xyx
 * @date :2019/6/26 14:51
 */
class ConfirmViewModel : BaseViewModel() {

    fun checkInfo(
        name: String,
        id: String,
        list: ArrayList<ConfirmBean>,
        successAction: (t: ConfirmSuccessBean, map: HashMap<String, Any>) -> Unit
    ) {
        if (list.size == 0) {
            return
        }
        val data = HashMap<String, Any>()
        val claim = HashMap<String, String>()
        XApplication.instance.setRole(RoleType.creditor)
        claim["name"] = name
        claim["idCard"] = id
        data["claim"] = claim
        data["debtors"] = list
        data["memberId"] = XApplication.instance.getUserInfo().member?.id ?: ""
        service.checkCreditorConfirm(data.getParam())
            .netDispatch(object : RxNetObserver<ConfirmSuccessBean>(true, "正在检测确权信息……") {
                override fun onError(msg: String) {
                    toast(msg)
                }

                override fun onStart() {

                }

                override fun onSuccess(t: ConfirmSuccessBean?) {
                    successAction(t!!, data)
                }
            })
    }

    fun postInfo(map: HashMap<String, Any>, action: () -> Unit) {
        service.postCreditorConfirm(map).netDispatch(object : RxNetObserver<Any>(true, "正在提交确权资料……") {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: Any?) {
                action()
            }
        })
    }

}