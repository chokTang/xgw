package com.wbg.xigui.bean

data class SupplierNewsBean(
    val amount: Double? = 0.0,
    val name: String? = "",
    val number: Int? = 0,
    val orderId: String? = "",
    val propertyValue: String? = "",
    val status: Int? = 0,
    val type:Int=1,//1商品2商家
    val thumbnail: String? = "",
    val trackingNo: String? = "",
    val updateTime: String? = ""
) : BaseBean()