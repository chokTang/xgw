package com.wbg.xigui.bean.body

/**
 * @author tyk
 * @date :2019/8/29 10:54
 * @des :
 */

data class RefundBody(
    var images: String? = "",//用户上传的图片id，多个用,号
    var memberId: String? = "",//memberId
    var memo: String? = "",//详细说明
    var mobile: String? = "",//买家联系电话
    var orderId: String? = "",//要退款/退货的订单号
    var reason: Int? = 0,//退款/退货原因,以前段选择框的顺序为准
    var received: Int? = 0,//0 未收到货 1已收到货物
    var skuId: String? = "",//要退款/退货的skuId
    var type: Int? = 1  //1 退款 2退货
)