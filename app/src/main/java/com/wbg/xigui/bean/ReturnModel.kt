package com.wbg.xigui.bean


data class ReturnModel<T>(
    val message: String? = "",
    val status: Int = 0,
    var error: String? = "",
    var data: T?
) : BaseBean() {
    val model: T? = null
}