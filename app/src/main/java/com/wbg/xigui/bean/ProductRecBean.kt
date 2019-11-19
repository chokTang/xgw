package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/15 9:57
 * @des : 推荐商品数据源
 */

data class ProductRecBean(
    val name: String? = "",
    val price: Float? = 0f,
    val productId: String? = "",
    val propertyValue: String? = "",
    val skuId: String? = "",
    val thumbnail: String? = ""
): BaseBean()