package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/9/5 14:52
 * @des :
 */
data class RecordBean(
    val haveNext: Boolean? = false,
    val list: List<RecordBeanList?>? = listOf()
): BaseBean()

data class RecordBeanList(
    val bond: Float? = 0f,
    val money: Float? = 0f,
    val operationTime: String? = "",
    val userIcon: String? = "",
    val userId: String? = "",
    val userName: String? = "",
    val userNcikName: String? = ""
): BaseBean()