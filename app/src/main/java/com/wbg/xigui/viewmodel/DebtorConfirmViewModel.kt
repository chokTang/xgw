package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.DebtorConfirmBean
import com.wbg.xigui.bean.Member
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import netDispatch
import startRout
import toast

/**
 * @author xyx
 * @date :2019/6/27 10:33
 */
class DebtorConfirmViewModel : BaseViewModel() {
    fun postInfo(name: String, id: String, list: ArrayList<DebtorConfirmBean>, successAction: () -> Unit) {
        if (list.size == 0) {
            return
        }
        var data = HashMap<String, Any>()
        var claim = HashMap<String, String>()
        XApplication.instance.setRole(RoleType.debtor)
        claim["name"] = name
        claim["idCard"] = id
        data["debtor"] = claim
        data["claim"] = list
        service.postDebtorConfirm(data.getParam()).netDispatch(object : RxNetObserver<Any>(true, "正在提交确权资料……") {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
                var userInfoBean = XApplication.instance.getUserInfo()
                userInfoBean.currentRole = RoleType.debtor
                userInfoBean.token = XApplication.instance.getToken()
                userInfoBean.member = Member(id = XApplication.instance.getMemberId())
                XApplication.instance.setUserInfo(userInfoBean)
                successAction()
            }
        })
    }
}