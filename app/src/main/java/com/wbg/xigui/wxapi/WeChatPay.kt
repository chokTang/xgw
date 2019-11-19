package com.wbg.xigui.wxapi

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import toast


/**
 * @Title WeChatPay.java
 * @date 2015-11-23
 */
class WeChatPay {
    private var api: IWXAPI? = null
    private var scenesId: String? = null
    private var activity: Activity? = null


    fun Pay(activity: Activity, apiPayInfo: APIPayInfo, scenesId: String) {
        api = WXAPIFactory.createWXAPI(activity, APP_ID)
        this.activity = activity
        this.scenesId = scenesId
        if (!api!!.isWXAppInstalled) {
            toast("您未安装微信!")
            return
        }
        doPay(apiPayInfo)
    }

    private fun doPay(apiPayInfo: APIPayInfo) {
        sendPayReq(apiPayInfo)
    }

    private fun sendPayReq(apiPayInfo: APIPayInfo) {
        val req = PayReq()
        req.appId = APP_ID
        req.partnerId = apiPayInfo.partnerid
        req.prepayId = apiPayInfo.prepayid
        req.packageValue = "Sign=WXPay"
        req.nonceStr = apiPayInfo.noncestr
        req.timeStamp = apiPayInfo.timestamp
        req.sign = apiPayInfo.sign

        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        if (api!!.registerApp(APP_ID)) {
            WXPayEntryActivity.scenesId = scenesId
            api!!.sendReq(req)
        }
    }


    private fun getIP(context: Context): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return intToIp(ipAddress)
    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." + (i shr 8 and 0xFF) + "." + (i shr 16 and 0xFF) + "." + (i shr 24 and 0xFF)
    }

    companion object {

        const val APP_ID = "wx2654d3fedf937f6b"

        val instance: WeChatPay
            get() = WeChatPay()
    }

}
