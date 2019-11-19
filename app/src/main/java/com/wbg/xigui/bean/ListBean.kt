package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/7/19 14:23
 */
data class ListBean<T>(
    val list: ArrayList<T>
) : BaseBean()