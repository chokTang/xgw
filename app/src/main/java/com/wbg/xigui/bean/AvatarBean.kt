package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/9/5 10:13
 * @des :
 */



data class AvatarBean(
    val list: MutableList<AvatarBeanList?>? = arrayListOf()
): BaseBean()

data class AvatarBeanList(
    var type:Int = 1,
    val id: String? = "",
    val userIcon: String? = "",
    val userName: String? = "",
    val userNcikName: String? = ""
): BaseBean()

