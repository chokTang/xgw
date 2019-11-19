package com.wbg.xigui.bean

import com.wbg.xigui.utils.StrUtil
import otherwise
import yes

data class GoodsBean(
    val cost: Float=0f,
    val marketPrice: Float=0f,
    val name: String?="",
    val id: String?="",
    val skuId: String?="",
    val percentage: Float?=0f,
    val price: Float=0f,
    val salesVolume: Int=0,
    val thumbnail: String?=""

) : BaseBean() {
    val img: String?
        get() {
            return thumbnail
        }
    val des: String?
        get() {
            return name.isNullOrEmpty().yes { "" }.otherwise { name }
        }

    val bood:String?
        get() {
            return StrUtil.subZeroAndDot(String.format("%.2f", (percentage!! * 100)))+"%"
        }
}