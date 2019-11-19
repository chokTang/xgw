package com.wbg.xigui.bean

import com.contrarywind.interfaces.IPickerViewData

/**
 * @author tyk
 * @date :2019/8/16 17:46
 * @des :
 */

data class CityBean(
    val children: List<Children?>? = listOf(),
    val code: String? = "",
    val name: String? = ""
):IPickerViewData {
    override fun getPickerViewText(): String {
        return name!!
    }
}
//这里实现 IPickerViewData变量是用来 传参的  因为当前view 的变量 第一个 第二个第三个 用同样的类型 去传参 否则要报ANR


data class Children(
    val children: List<ChildrenX?>? = listOf(),
    val code: String? = "",
    val name: String? = ""
):IPickerViewData{
    override fun getPickerViewText(): String {
        return name!!
    }

}

data class ChildrenX(
    val code: String? = "",
    val name: String? = ""
):IPickerViewData {
    override fun getPickerViewText(): String {
        return name!!
    }
}