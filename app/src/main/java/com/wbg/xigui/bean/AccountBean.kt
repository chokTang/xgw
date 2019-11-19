package com.wbg.xigui.bean

data class AccountBean(
    val availableBond: Double,
    val balance: Double,
    val bond: Double,
    val bondDoubleerestRate: Double,
    val bondWithdrawal: Double,
    val createTime: String?,
    val delStatus: String?,
    val frozen: Double,
    val frozenFrozen: Double,
    val id: String?,
    val moneyWithdrawal: Double,
    val surplusBond: Double,
    val thawBond: Double,
    val updateTime: String?,
    val userId: String?,
    val userRole: String?,
    val member: Member?
) : BaseBean()