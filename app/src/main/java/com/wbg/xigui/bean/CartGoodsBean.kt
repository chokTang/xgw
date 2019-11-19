package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/6/17 14:44
 */
data class CartGoodsBean(
        var isSelected: Boolean = false,
        val bond: Float? = 0f,
        val merchantId: String? = "",
        val merchantName: String? = "",
        val name: String? = "",
        val price: Float? = 0f,
        val productId: String? = "",
        val propertyValue: String? = "",
        val skuId: String? = "",
        val stock: Int? = 0,
        val thumbnail: String? = "",
        var total: Int? = 1
) : BaseBean()