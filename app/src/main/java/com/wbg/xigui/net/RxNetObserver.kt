package com.wbg.xigui.net

import com.wbg.xigui.XApplication
import com.wbg.xigui.bean.ReturnModel
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.LoginActivity
import com.wbg.xigui.utils.DialogUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import log
import startRout
import yes

abstract class RxNetObserver<T> : Observer<ReturnModel<T>> {
    var showLoading = false
    var canCancel = true
    var loadingText = ""

    constructor(showLoading: Boolean, text: String = "正在加载数据……") : this(showLoading, text, true)
    constructor() : this(false)
    constructor(showLoading: Boolean, text: String = "正在加载数据……", canCancel: Boolean) {
        this.showLoading = showLoading
        this.canCancel = canCancel
        this.loadingText = text
    }

    override fun onSubscribe(d: Disposable) {
        showLoading.yes { DialogUtil.showLoading(loadingText, XApplication.currentActivity, canCancel) }
        onStart()
    }

    override fun onNext(t: ReturnModel<T>) {
        showLoading.yes { DialogUtil.hideLoading() }
        when {
            t.status == 1000 -> try {
                onSuccess(t.data)
            } catch (e: Throwable) {
                onError(e.message ?: "")
            }
            t.status == 401 -> {
                onError("请重新登录")
                XApplication.instance.logout()
                if (XApplication.currentActivity !is LoginActivity) {
                    startRout(RoutUrl.Common.login)
                }
            }
            else -> onError(t.message ?: "")
        }
    }

    override fun onError(e: Throwable) {
        showLoading.yes { DialogUtil.hideLoading() }
        onError(e.message ?: "未知错误")
        log(e.message ?: "未知错误")
    }

    override fun onComplete() {

    }

    abstract fun onStart()
    abstract fun onError(msg: String)
    abstract fun onSuccess(t: T?)
}