package com.wbg.xigui.viewmodel

import android.os.Message
import com.alipay.sdk.app.PayTask
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.PayBean
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.pay.PayResult
import com.wbg.xigui.wxapi.WeChatPay
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import log
import netDispatch
import toast


/**
 * @author xyx
 * @date :2019/6/20 16:22
 */
class PayStoreViewModel : BaseViewModel() {
    var need2success = false
    fun getPayParam(bean: StoreBean, type: Int, num: Double) {
        val map = HashMap<String, Any>()
        map["amount"] = num
        map["merchant"] = bean.id ?: ""
        map["payment"] = type
        service.getPayParam(map).netDispatch(object : RxNetObserver<PayBean>(true, "正在获取支付信息……") {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: PayBean?) {
                t?.run {
                    if (type == 2) {
                        WeChatPay.instance.Pay(XApplication.currentActivity, wxPayBody!!, "")
                    } else {
                        alipay(this)
                    }
                }
            }
        })
    }

    fun alipay(bean: PayBean) {
        Observable.create(ObservableOnSubscribe<Message> { e ->
            val alipay = PayTask(XApplication.currentActivity)
            val result = alipay.payV2(bean.alipayPayBody, true)
            log("===回调==" + result["result"])
            val msg = Message()
            msg.obj = result
            e.onNext(msg)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Message> {
                override fun onSubscribe(d: Disposable) {
                    log("===开始了==")
                }

                override fun onNext(o: Message) {
                    log("===回调进来了==" + o.obj)
                    val result = PayResult(o.obj as Map<String, String>)
                    if (result.resultStatus == "9000") {//支付成功
                        need2success = true
                    } else {//支付失败
                        toast("支付失败")
                        log("支付失败--${result.resultStatus}")
                    }
                }

                override fun onError(e: Throwable) {
                    toast(e.message ?: "")
                    log("===回调失败了==")
                }

                override fun onComplete() {
                    log("===回调完成了==")
                }
            })
    }

    fun getBond(id: String, block: (bean: StoreBean) -> Unit) {
        val map = HashMap<String, Any>()
        map["merchantId"] = id
        service.getStoreBond(map).netDispatch(object : RxNetObserver<StoreBean>() {
            override fun onError(msg: String) {
            }

            override fun onStart() {
            }

            override fun onSuccess(t: StoreBean?) {
                t?.run {
                    block(this)
                }
            }
        })
    }

}