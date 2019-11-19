package com.wbg.xigui.bean

data class PaymentDetailBean(
    val createTime: String? = "",
    val incomeTime: String? = "",
    var totalAmt: Double? = 0.0,
    val userId: String? = "",
    val totalNumber:Int?=0
) : BaseBean()