package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/26 16:21
 * @des : 退款退货订单
 */
data class RefundBean(
    val imageIds: String? = "",
    val images: List<String?>? = listOf(),
    val memo: String? = "",
    val orderId: String? = "",
    val percentage: Float? = 0f,
    val price: Float? = 0f,
    val productName: String? = "",
    val propertyValue: String? = "",
    val refundId: String? = "",
    val refuseReason: String? = "",
    val skuId: String? = "",
    val status: Int? = 0,//0  处理中  1 已经处理完毕  2拒绝
    val thumbnail: String? = "",
    val total: Int? = 0
): BaseBean()