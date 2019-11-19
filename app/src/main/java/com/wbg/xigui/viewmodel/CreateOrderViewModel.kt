package com.wbg.xigui.viewmodel

import android.os.Message
import com.alipay.sdk.app.PayTask
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.AddressBean
import com.wbg.xigui.bean.CreateOrderPamas
import com.wbg.xigui.bean.PayOrderBean
import com.wbg.xigui.dialog.AlertDialogFragment
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.pay.PayResult
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.AccountActivity.Companion.KEY_POSITION
import com.wbg.xigui.ui.order.CreateOrderActivity
import com.wbg.xigui.utils.PamasUtil
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
import startRout
import toast

/**
 * @author tyk
 * @date :2019/8/15 16:18
 * @des :  生成订单 viewmodel
 */

class CreateOrderViewModel : BaseViewModel(){

    var context: CreateOrderActivity?=null
    var orderId = ""

    /**
     * 生成商品订单
     */
    fun createProductOrder(pamas: CreateOrderPamas,block: (paySuccess: Boolean) -> Unit){
        service.createProductOrder(PamasUtil.objectToMap(pamas) as HashMap<String, @JvmSuppressWildcards Any>).netDispatch(object : RxNetObserver<PayOrderBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: PayOrderBean?) {

                when(t?.retCode){
                    "1000"->{
                        if (t.payment == 2) {
                            orderId = t.orderId!!
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
                    "1012"->{//债权提示
                        AlertDialogFragment.newIntance().setTitle("温馨提示")
                            .setContent("尊敬的债权人，您的债权已收回60%，请与您的债务人按照约定完成《债务结清合同》")
                            .setCancleBtn {  }
                            .setSureBtn("立即前往") {
                                startRout(RoutUrl.Mine.account_info,KEY_POSITION,1)
                            }.show(context!!.supportFragmentManager,"to_account_activity")
                    }
                    "1025"->{//未确权   提示确权对话框
                        AlertDialogFragment.newIntance().setTitle("温馨提示")
                            .setContent("购买商品还需要支付部分债权，您还没有债权，您可以在个人中心添加债权!")
                            .setCancleBtn {  }
                            .setSureBtn("去添加") {//跳入 选择 页面
                                startRout(RoutUrl.Common.creditor_confirm)
                            }.show(context!!.supportFragmentManager,"to_zq")
                    }
                    else ->{
                        toast(t?.msg!!)
                    }
                }

            }
        })
    }


    fun alipay(bean: PayOrderBean,block: (paySuccess: Boolean) -> Unit) {
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



    /**
     * 获取地址列表  这里是为了拿到默认地址
     */
    fun getAddressList(block: (AddressBean) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId() ?: ""
        map["page"] = 1
        map["size"] = 20
        service.getAddressList(map).netDispatch(object : RxNetObserver<AddressBean>() {
            override fun onStart() {
            }

            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onSuccess(t: AddressBean?) {
                t?.run {
                    block(this)
                }
            }

        })
    }


}