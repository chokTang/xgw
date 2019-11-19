package com.wbg.xigui.bean

data class UserInfoBean(
    val extendInfo: ExtendInfo?=null,
    val flag: String?=null,//新老用户：0-老用户；1-新用户
    var member: Member?=null,
    val retCode: String?=null,
    val retMsg: String?=null,
    val roles: List<String>?= arrayListOf(),
    var token: String?=null,
    var currentRole: String?=null
) : BaseBean()