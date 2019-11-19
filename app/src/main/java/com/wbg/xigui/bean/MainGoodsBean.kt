package com.wbg.xigui.bean

import no
import otherwise

/**
 * @author xyx
 * @date :2019/7/3 15:13
 */
data class MainGoodsBean(
    val chwl: ArrayList<GoodsBean>?,
    val jptj: ArrayList<GoodsBean>?,
    val rmsp: ArrayList<GoodsBean>?
) : BaseBean() {
    val hot: ArrayList<GoodsBean>?
        get() {
            return rmsp.isNullOrEmpty().no { rmsp }.otherwise { ArrayList() }
        }
    val play: ArrayList<GoodsBean>?
        get() {
            return chwl.isNullOrEmpty().no { chwl }.otherwise { ArrayList() }
        }
    val jing: ArrayList<GoodsBean>?
        get() {
            return jptj.isNullOrEmpty().no { jptj }.otherwise { ArrayList() }
        }
}