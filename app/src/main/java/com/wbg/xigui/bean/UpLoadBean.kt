package com.wbg.xigui.bean

data class UpLoadBean(
    val imgId: String? = null,
    val imgUrl: String? = null,
    val retCode: String? = null,
    val retMsg: String? = null
) : BaseBean()