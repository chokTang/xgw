package com.wbg.xigui.bean

data class StoreDetailBean(
    val address: String? = "",
    val album: String? = "",
    val bond: Double? = 0.0,
    val distance: Double? = 0.0,
    val id: String? = "",
    val images: ArrayList<String>? = ArrayList(),
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val mobile: String? = "",
    val name: String? = "",
    val product: String? = "",
    val sale: Int? = 0,
    val score: Double = 0.0,
    val serviceTime: String? = "",
    val thumbnail: String? = ""
) : BaseBean()