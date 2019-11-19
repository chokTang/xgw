package com.wbg.xigui.bean

data class Member(
    val createTime: String? = "",
    val delStatus: String? = "",
    val expireFreshTime: Long? = 0,
    val expireMinite: Int = 0,
    val familyFlag: String? = "",
    val familyUserId: String? = "",
    val id: String? = "",
    val loginTime: String? = "",
    val updateTime: String? = "",
    val userAuthenStatus: String? = "",
    val userCradid: String? = "",
    val userFlag: String? = "",
    val userFrom: String? = "",
    val userIcon: String? = "",
    val userName: String? = "",
    val userNcikName: String? = "",
    val userPhone: String? = "",
    val userPwd: String? = "",
    val userStatus: String? = "",
    val userToken: String? = ""
) : BaseBean()