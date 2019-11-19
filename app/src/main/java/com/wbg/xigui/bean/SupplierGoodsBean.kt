package com.wbg.xigui.bean

data class SupplierGoodsBean(
    val id: String? = "",
    val itemUpshelf: Int? = 0,
    val name: String? = "",
    val number: Int = 0,
    val price: Double = 0.0,
    val propertyValue: String? = "",
    val stock: Int = 0,
    val thumbnail: String? = ""
) : BaseBean()