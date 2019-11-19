package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.base.BaseViewModel

/**
 * @author xyx
 * @date :2019/7/16 10:15
 */
class SupplierNewsViewModel:BaseViewModel() {
    val fragmentType = MutableLiveData<Int>()
    fun changeFragment(type: Int) {
        fragmentType.value = type
    }
}