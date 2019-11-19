package com.wbg.xigui.bean

data class BankBean(
    var color:Int = 0,
    val accountName: String? = "",
    var accountNo: String? = "",
    val accountType: String? = "",
    var bankName: String? = "",
    val bankOpenName: String? = "",
    val bankType: String? = "",
    val billBankName: String? = "",
    val city: String? = "",
    val createTime: String? = "",
    val defaultStatus: String? = "",
    val delStatus: String? = "",
    val id: String? = "",
    val province: String? = "",
    val updateTime: String? = "",
    val userId: String? = "",
    val userPhone: String? = "",
    val verify: String? = ""
) : BaseBean()