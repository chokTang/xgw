package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/6/27 11:31
 */
class MsgBean(
    var id: String? = "",
    val createTime: String? = null,
    val messageContent: String? = null,
    val messageTitle: String? = null,
    val jumpParams: String? = null,
    val jumpType: String? = null,
    val userStatus: String? = null
) : BaseBean()