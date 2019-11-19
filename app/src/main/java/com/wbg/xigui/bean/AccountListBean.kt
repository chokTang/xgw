package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/7/23 14:32
 */
data class AccountListBean(
    val list: ArrayList<AccountRightBean>?= arrayListOf(),
    val noSureList: ArrayList<AccountRightBean>? = arrayListOf(),
    val sureList: ArrayList<AccountRightBean>?= arrayListOf()
) : BaseBean()