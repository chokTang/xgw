package com.wbg.xigui.bean

data class BannerBean(
    val domain: String,
    val link: String,
    val memo: String,
    val name: String,
    val path: String
) : BaseBean() {
    var img: String? = ""
        get() {
            return domain
        }
}