package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/6/19 15:11
 */
data class CommentBean(
    var head: String? = "",
    val album: String? = "",
    val comment: String? = "",
    val name: String? = "",//商品名称
    val counts: Int? = 0,
    val images: ArrayList<String>? = ArrayList(),
    val score: Double = 0.0,
    val userNickName: String? = "",
    val userPhone: String? = ""
) : BaseBean() {
    val imgs: ArrayList<String>
        get() {
            return images ?: ArrayList()
        }
}