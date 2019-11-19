package com.wbg.xigui.bean

import com.wbg.xigui.wxapi.APIPayInfo

data class PayBean(
    val alipayPayBody: String? = "",
    val payment: Int? = 0,
    val wxPayBody: APIPayInfo?
)