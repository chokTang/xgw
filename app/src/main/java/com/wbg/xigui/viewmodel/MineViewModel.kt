package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.AccountBean
import com.wbg.xigui.bean.ChooseRoleBean
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.OrderActivity.Companion.KEY_TYPE_POSITION
import com.wbg.xigui.ui.mine.ShareActivity.Companion.KEY_TYPE
import netDispatch
import otherwise
import startRout
import toast
import yes

/**
 * @author xyx
 * @date :2019/6/18 9:35
 */
class MineViewModel : BaseViewModel() {
    val bean = MutableLiveData<AccountBean>()
    fun goExchange() {
        startRout(RoutUrl.Mine.exchange)
    }

    fun addRights() {
//        startRout(RoutUrl.Mine.choice)
        startRout(RoutUrl.Common.creditor_confirm)
    }

    fun familyPay() {
        startRout(RoutUrl.Mine.family_pay)
    }

    fun getUserInfo() {
        if (XApplication.isLogin()) {
            val map = HashMap<String, Any>()
            map["role"] = XApplication.instance.getUserInfo().currentRole ?: ""
            service.getUserInfo(map.getParam()).netDispatch(object : RxNetObserver<AccountBean>() {
                override fun onError(msg: String) {
                    toast(msg)
                }

                override fun onStart() {

                }

                override fun onSuccess(t: AccountBean?) {
                    t?.run {
                        bean.value = this
                        var userinfo = XApplication.instance.getUserInfo().copy()
                        userinfo.member = member
                        XApplication.instance.setUserInfo(userinfo)
                    }

                }
            })
        }
    }

    fun goSetting() {
        startRout(RoutUrl.Mine.setting)
    }

    fun goOrder(type: Int) {
        startRout(RoutUrl.Mine.order, KEY_TYPE_POSITION, type)
    }

    fun goRefund() {
        startRout(RoutUrl.Mine.order_refund)
    }

    fun goAccount() {
        startRout(RoutUrl.Mine.account_info)
    }

    fun goAddress() {
        startRout(RoutUrl.Mine.addressList)
    }

    fun goBankManager() {
        startRout(RoutUrl.Mine.bankcard_manage)
    }


    fun goShare() {
        startRout(RoutUrl.Mine.share,KEY_TYPE,2)

    }


    fun chooseRole(role: String) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getUserInfo().member?.id ?: ""
        map["role"] = role
        service.chooseRole(map.getParam())
            .netDispatch(object : RxNetObserver<ChooseRoleBean>(true) {
                override fun onError(msg: String) {
                    toast(msg)
                    XApplication.instance.setToken("")
                }

                override fun onStart() {
                }

                override fun onSuccess(t: ChooseRoleBean?) {
                    val userInfoBean = XApplication.instance.getUserInfo()
                    t?.run {
                        userInfoBean.currentRole = role
                        XApplication.instance.setUserInfo(userInfoBean)
                        XApplication.instance.setRole(role)
                        when (role) {
                            RoleType.creditor -> {//债权人
                                (newFlag == "1").yes {
                                    //老用户
                                    startRout(RoutUrl.Main.home_activity)
                                    XApplication.instance.clearActivity()
                                }.otherwise {
//                                    startRout(RoutUrl.Common.creditor_confirm)
                                    toast("您还未确权")
                                    startRout(RoutUrl.Main.home_activity)
                                    XApplication.instance.clearActivity()
                                }
                            }
                            RoleType.debtor -> {//债务人
                                (newFlag == "1").yes {
                                    //老用户
                                    startRout(RoutUrl.Debtor.home)
                                    XApplication.instance.clearActivity()
                                }.otherwise {
//                                    startRout(RoutUrl.Common.debtor_confirm)
                                    toast("您还未确权")
                                    startRout(RoutUrl.Debtor.home)
                                    XApplication.instance.clearActivity()
                                }
                            }
                            RoleType.agent -> {//代理商
                                startRout(RoutUrl.Agent.home)
                                XApplication.instance.clearActivity()
                            }
                            RoleType.supplier -> {//商家
                                startRout(RoutUrl.Supplier.home)
                                XApplication.instance.clearActivity()
                            }
                        }
                    }
                }
            })
    }
}