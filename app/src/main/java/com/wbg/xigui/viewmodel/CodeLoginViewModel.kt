package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ChooseRoleBean
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.bean.UserInfoBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.push.TagAliasOperatorHelper
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.StrUtil
import netDispatch
import otherwise
import startRout
import toast
import yes

/**
 * @author xyx
 * @date :2019/6/24 12:03
 */
class CodeLoginViewModel : BaseViewModel() {
    val phone = MutableLiveData<String>()
    val code = MutableLiveData<String>()

    fun login(block: (t: UserInfoBean?) -> Unit) {
        if (!StrUtil.isMobileNo(phone.value)) {
            toast("请输入正确的手机号")
            return
        }
        if (StrUtil.isEmpty(code.value)) {
            toast("请输入验证码")
            return
        }
        val map = HashMap<String, Any>()
        map["phone"] = phone.value!!
        map["code"] = code.value!!
        service.login(map.getParam()).netDispatch(object : RxNetObserver<UserInfoBean>(true, "正在登录……") {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: UserInfoBean?) {


                //设置别名
                val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
                tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
                tagAliasBean.alias = t?.member?.id
                tagAliasBean.isAliasAction = true
                tagAliasBean.tags.add(RoleType.creditor)
                TagAliasOperatorHelper.sequence++
                TagAliasOperatorHelper.getInstance().handleAction(XApplication.instance, TagAliasOperatorHelper.sequence, tagAliasBean)

                XApplication.instance.setToken(t?.token)
                XApplication.instance.setMemberId(t?.member?.id)
                t?.run {
                        val userInfoBean = this
                        userInfoBean.currentRole = RoleType.creditor
                        XApplication.instance.setUserInfo(userInfoBean)
                        XApplication.instance.setRole( RoleType.creditor)
                        startRout(RoutUrl.Main.home_activity)
                        XApplication.instance.clearActivity()
                }

            }
        })
    }

    fun getCode(block: () -> Unit) {
        if (!StrUtil.isMobileNo(phone.value)) {
            toast("请输入正确的手机号")
            return
        }
        val map = HashMap<String, Any>()
        map["phone"] = phone.value!!
        map["type"] = 1
        service.sendCode(map.getParam()).netDispatch(object : RxNetObserver<Any>(true) {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
                block()
                toast("验证码发送成功")
            }
        })
    }

    fun chooseRole(userInfoBean: UserInfoBean, role: String) {
        val map = HashMap<String, Any>()
        map["memberId"] = userInfoBean.member?.id ?: ""
        map["role"] = role
        service.chooseRole(map.getParam()).netDispatch(object : RxNetObserver<ChooseRoleBean>(true) {
            override fun onError(msg: String) {
                toast(msg)
                XApplication.instance.setToken("")
            }

            override fun onStart() {
            }

            override fun onSuccess(t: ChooseRoleBean?) {
                val userInfoBean = userInfoBean
                t?.run {
                    if (!((role == RoleType.creditor || role == RoleType.debtor) && newFlag != "1")) {
                        userInfoBean.currentRole = role
                        XApplication.instance.setUserInfo(userInfoBean)
                    }
                    when (role) {
                        RoleType.creditor -> {//债权人
                            (newFlag == "1").yes {
                                //老用户
                                startRout(RoutUrl.Main.home_activity)
                                XApplication.instance.clearActivity()
                            }.otherwise {
                                startRout(RoutUrl.Common.creditor_confirm)
                            }
                        }
                        RoleType.debtor -> {//债务人
                            (newFlag == "1").yes {
                                //老用户
                                startRout(RoutUrl.Debtor.home)
                                XApplication.instance.clearActivity()
                            }.otherwise {
                                startRout(RoutUrl.Common.debtor_confirm)
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