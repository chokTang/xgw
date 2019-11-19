package com.wbg.xigui.rout

import android.content.Context
import android.os.Looper
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.wbg.xigui.XApplication
import log
import otherwise
import startRout
import yes
import java.util.*

@Interceptor(priority = 1, name = "LoginInterceptor")
class LoginInterceptor : IInterceptor {
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        if (postcard != null && postcard.extra == RoutUrl.Extra.login) {
            val isLogin = !XApplication.instance.getUserInfo().token.isNullOrEmpty()
            isLogin.yes {
                callback?.onContinue(postcard)
            }.otherwise {
                //拦截跳转到登录页面
                log("${Looper.getMainLooper().thread.id == Thread.currentThread().id}")
                startRout(RoutUrl.Common.login)
            }
        } else {
            callback?.onContinue(postcard)
        }
    }

    override fun init(context: Context?) {
    }
}