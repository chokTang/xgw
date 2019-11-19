package com.wbg.xigui.bean

data class StoreBean(
    val address: String? = "",
    val album: String? = "",
    var bond: Float = 0.0f,
    val distance: String? = "",
    var id: String? = "",
    val images: ArrayList<String>? = ArrayList(),
    val latitude: String? = "",
    val longitude: String? = "",
    val mobile: String? = "",
    var name: String? = "",
    val product: String? = "",
    val score: Float = 0f,
    val serviceTime: String? = "",
    val thumbnail: String? = "",
    val sale: Int = 0
) : BaseBean() {
    val img: String?
        get() {
            return thumbnail ?: ""
        }
}