package com.wbg.xigui.bean

data class AccountRightBean(
    val availableBond: Double = 0.0,
    val bond: Double = 0.0,
    val contractFlag: String = "",//    合同签订状态（-1-无,0-初始,1-通知,2-完成）
    val createTime: String? = "",
    val creditorIcon: String? = "",
    val creditorUserCradid: String? = "",
    val creditorUserId: String? = "",
    val creditorUserName: String? = "",
    val id: String? = "",
    val obligorIcon: String? = "",
    val obligorUserCradid: String? = "",
    val obligorUserId: String? = "",
    val obligorUserName: String? = "",
    val priority: Int? = 0,
    val settleFlag: String? = "",
    val sort: Int? = 0,
    val sure1Flag: String? = "",
    val sure2Flag: String? = "",
    val surplusBond: Double = 0.0,
    var sure: Boolean = false,//是否确权
    var seleted: Boolean = false,//是否优先
    var visibleHead: Boolean = false//是否显示title
) : BaseBean()