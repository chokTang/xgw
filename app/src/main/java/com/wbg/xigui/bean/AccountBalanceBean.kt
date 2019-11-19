package com.wbg.xigui.bean

data class AccountBalanceBean(
    val availableBalance: Double = 0.0,
    val balance: Double = 0.0,
    val consumptionCount: Double = 0.0,
    val highestAmount: Double = 0.0,
    val surplusBond: Double = 0.0,
    val totalAmount: Double = 0.0,
    val withdrawalBalanece: Double = 0.0,
    val bond: Double = 0.0
) : BaseBean()