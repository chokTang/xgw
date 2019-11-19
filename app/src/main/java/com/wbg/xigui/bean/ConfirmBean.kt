package com.wbg.xigui.bean

import otherwise
import yes

/**
 * @author xyx
 * @date :2019/6/26 15:30
 */
data class ConfirmBean(
    var name: String = "",
    var idCard: String = "",
    var phone: String = "",
    var hasFile: Boolean = true,
    var img: String = "",
    var imgId: String = "",
    var hasFileAmount: Double = 0.0,
    var noFileAmount: Double = 0.0,
    var docId: String = "",
    var court: String = "",
    var judgmentTime: String = "",
    var rate: Double = 24.0
) : BaseBean() {
    var interest: Double = rate / 100
    var wsFlag: String = hasFile.yes { "0" }.otherwise { "1" }
    var amount: Double = hasFile.yes { hasFileAmount }.otherwise { noFileAmount }
}