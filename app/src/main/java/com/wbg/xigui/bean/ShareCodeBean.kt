package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/9/6 17:31
 * @des :
 */
data class ShareCodeBean(
    val code: String? = "",
    val retCode: String? = "",
    val retMsg: String? = ""
): BaseBean()