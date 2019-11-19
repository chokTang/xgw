package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.utils.StrUtil
import netDispatch
import otherwise
import toast
import yes

/**
 * @author xyx
 * @date :2019/6/26 9:46
 */
class SetPwdViewModel : BaseViewModel() {
    val pwd = MutableLiveData<String>()
    val code = MutableLiveData<String>()
    val rePwd = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    var type = 1//1为忘记密码2为修改密码

    fun getCode(successAction: () -> Unit) {
        if (type == 1 && StrUtil.isEmpty(phone.value)) {
            toast("请输入手机号码")
            return
        }
        val map = HashMap<String, Any>()
        map["type"] = 2
        map["phone"] =
            (type == 1).yes { phone.value!! }.otherwise { XApplication.instance.getUserInfo().member?.userPhone ?: "" }
        service.sendCode(map.getParam()).netDispatch(object : RxNetObserver<Any>(true) {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
                successAction()
                toast("验证码发送成功")
            }
        })

    }

    fun setPwd(action: () -> Unit) {
        val map = HashMap<String, Any>()
        map["phone"] =
            (type == 1).yes { phone.value!! }.otherwise { XApplication.instance.getUserInfo().member?.userPhone ?: "" }
        map["pwd"] = pwd.value!!
        map["code"] = code.value!!
        service.setPwd(map).netDispatch(object : RxNetObserver<Any>(true, "正在修改密码……") {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: Any?) {
                toast("密码修改成功")
                action()
            }
        })
    }

    fun sure(action: () -> Unit) {
        if (type == 1 && StrUtil.isEmpty(phone.value)) {
            toast("请输入手机号码")
            return
        }
        if (StrUtil.isEmpty(code.value)) {
            toast("请输入验证码")
            return
        }
        if (StrUtil.isEmpty(pwd.value)) {
            toast("请输入密码")
            return
        }
        if (StrUtil.isEmpty(rePwd.value)) {
            toast("请确认密码")
            return
        }
        if (!pwd.value.equals(rePwd.value)) {
            toast("密码前后不一致")
            return
        }
        setPwd(action)
    }

}