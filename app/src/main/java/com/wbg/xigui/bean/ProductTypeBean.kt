package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/28 11:39
 * @des : 分类品类列表
 */

data class ProductTypeBean(
        var selected: Boolean = false,

        val createTime: String? = "",
        val creator: String? = "",
        val id: String? = "",
        val keyword: String? = "",
        val name: String? = "",
        val number: Int? = 0,
        val parent: String? = "",
        val rank: Int? = 0,
        val reserve1: String? = "",
        val reserve2: String? = "",
        val reserve3: String? = "",
        val reserve4: String? = "",
        val reserve5: String? = "",
        val showInList: Int? = 0,
        val showInNavigation: Int? = 0,
        val status: Int? = 0,
        val thumbnail: String? = "",
        val updateTime: String? = ""
) : BaseBean()