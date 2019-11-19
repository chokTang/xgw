package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/29 19:10
 * @des :
 */
data class SearchProduct(
    val percentage: String? = "",
    val price: Float? = 0f,
    val productId: String? = "",
    val productName: String? = "",
    val propertyValue: String? = "",
    val sale: Int? = 0,
    val skuId: String? = "",
    val thumbnail: String? = ""
): BaseBean()