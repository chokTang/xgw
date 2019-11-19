package com.wbg.xigui.bean.body

/**
 * @author tyk
 * @date :2019/8/26 14:23
 * @des : 添加评论请求体
 */
data class AddCommentBody(
    var album: String? = "",
    var comment: String? = "",
    var memberId: String? = "",
    var productId: String? = "",
    var score: Float? = 0f
)