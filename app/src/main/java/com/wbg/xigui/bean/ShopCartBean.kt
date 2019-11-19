package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/6/17 14:41
 */
data class ShopCartBean(
        var isSelected: Boolean = false,
        var merchantName: String? = "",
        var goodsList: MutableList<CartGoodsBean>? = ArrayList()
) : BaseBean()