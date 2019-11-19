package com.wbg.xigui.viewmodel

import android.os.Message
import com.alipay.sdk.app.PayTask
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.FlagBean
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.bean.PayOrderBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.pay.PayResult
import com.wbg.xigui.widget.ObservableReplaceArrayList
import com.wbg.xigui.wxapi.APIPayInfo
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
 * @author tyk
 * @date :2019/7/5 16:20
 */
class OrderFragmentViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<OrderBean>()
    var type = 0


    /**
     * 获取订单列表
     */
    fun getData(block: (list: List<OrderBean>?) -> Unit) {
        val map = HashMap<String, Any>()
        map["page"] = mPage
        map["size"] = 20
        map["status"] = type
        service.getOrderList(map).netDispatch(object : RxNetObserver<List<OrderBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
                changeLoadType(list, null, true)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<OrderBean>?) {
                mPage++
                t?.run {
                    list.addAll(this)
                }
                block(list)
                changeLoadType(list, t, false)
            }
        })
    }

    fun refresh(block: (list: List<OrderBean>?) -> Unit) {
        mPage = 1
        list.clear()
        getData(block)
    }


    /**
     * 删除订单
     */
    fun deleteOrder(orderId: String?, block: (bean: FlagBean?) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        service.deleteOrder(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }

    /**
     * 提醒发货
     */
    fun remind(orderId: String? ,skuId: String?, block: (bean: FlagBean?) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        map["skuId"] = skuId!!
        service.remind(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }


    /**
     * 完成订单
     */
    fun completeOrder(orderId: String?, skuId: String?, block: (bean: FlagBean?) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        map["skuId"] = skuId!!
        service.completeOrder(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }

    /**
     * 取消订单
     */
    fun cancelOrder(orderId: String?, block: (bean: FlagBean?) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        service.cancelOrder(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }


    /**
     * 重新发起支付
     */
    fun rePay(orderId: String?,payment:Int, block: (paySuccess: Boolean) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["orderId"] = orderId!!
        map["payment"] = payment
        service.rePay(map).netDispatch(object : RxNetObserver<PayOrderBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: PayOrderBean?) {
                pay(t!!,block)
            }
        })
    }

    /**
     * 支付
     */
    fun pay(t:PayOrderBean,block: (paySuccess: Boolean) -> Unit){
        if (t.payment == 2) {
            val wxPayBody = APIPayInfo()
            wxPayBody.partnerid = t.wxPayBody?.partnerid
            wxPayBody.prepayid = t.wxPayBody?.prepayid
            wxPayBody.noncestr = t.wxPayBody?.noncestr
            wxPayBody.timestamp = t.wxPayBody?.timestamp
            wxPayBody.sign = t.wxPayBody?.sign
            WeChatPay.instance.Pay(XApplication.currentActivity, wxPayBody, "")
        } else {
            alipay(t,block)
        }
    }


    fun alipay(bean: PayOrderBean, block: (paySuccess: Boolean) -> Unit) {
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
                        block(true)
                        toast("支付成功")
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



}