package com.wbg.xigui.bean

data class PageListBean<T>(
    val data: ArrayList<T>? = null,
    val count: Int? = 0,
    val haveNext: Boolean? = false,
    val retCode: String? = null,
    val retMsg: String? = null,
    val total: Int? = 0
) : BaseBean()