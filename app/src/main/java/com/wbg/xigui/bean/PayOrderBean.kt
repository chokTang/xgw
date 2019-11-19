package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/20 11:06
 * @des :
 */
data class PayOrderBean(
    val alipayPayBody: String? = "",
    val appletPayBody: String? = "",
    val msg: String? = "",
    val orderId: String? = "",
    val payment: Int? = 0,
    val retCode: String? = "",
    val retMsg: String? = "",
    val wxPayBody: WxPayBody? = WxPayBody()
): BaseBean()


data class WxPayBody(
    val appid: String? = "",
    val noncestr: String? = "",
    val packages: String? = "",
    val partnerid: String? = "",
    val prepayid: String? = "",
    val sign: String? = "",
    val timestamp: String? = ""
)