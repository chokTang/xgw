package com.wbg.xigui.bean

data class MoneyRecordBean(
    val bond: Double? = 0.0,
    val bondFlag: String? = "",
    val bondRemarks: String? = "",
    val familyFlag: String? = "",
    val money: Double? = 0.0,
    val operationTime: String? = "",
    val otherUserId: String? = "",
    val userId: String? = ""
) : BaseBean()