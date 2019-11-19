package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/16 11:10
 * @des : 地址数据源
 */

data class AddressBean(
    val haveNext: Boolean? = false,
    val list: List<X?>? = listOf(),
    val retCode: String? = "",
    val retMsg: String? = "",
    val total: Int? = 0
): BaseBean()

data class X(
    val city: String? = "",
    val cityCode: String? = "",
    val defaultStatus: String? = "",
    val detailAddress: String? = "",
    val id: String? = "",
    val name: String? = "",
    val phoneNumber: String? = "",
    val postCode: String? = "",
    val province: String? = "",
    val region: String? = ""
): BaseBean()