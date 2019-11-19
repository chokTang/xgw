package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/7/25 16:54
 */
data class ScanCodeBean(
    val name: String? = "",
    val id: String? = "",
    val bond: Float = 0.0f
) : BaseBean()