package com.wbg.xigui.bean

data class StoreTypeBean(
    val createTime: String? = "",
    val creator: String? = "",
    val id: String? = "",
    val name: String? = "",
    val reserve1: String? = "",
    val reserve2: String? = "",
    val reserve3: String? = "",
    val reserve4: String? = "",
    val reserve5: String? = "",
    val status: Int? = 0,
    val thumbnail: String? = "",
    val updateTime: String? = ""
) : BaseBean()