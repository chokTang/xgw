package com.wbg.xigui.bean

data class ConfirmSuccessBean(
    val balance: Double = 0.0,
    val bond: Double = 0.0,
    val bondInterestRate: Double = 0.0,
    val moneyWithdrawal: Double = 0.0,
    val retCode: String,
    val retMsg: String,
    val surplusBond: Double = 0.0
) : BaseBean()