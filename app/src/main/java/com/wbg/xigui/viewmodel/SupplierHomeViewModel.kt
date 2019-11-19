package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.base.BaseViewModel

/**
 * @author xyx
 * @date :2019/7/11 9:34
 */
class SupplierHomeViewModel : BaseViewModel() {
    val fragmentType = MutableLiveData<Int>()
    fun changeFragment(type: Int) {
        fragmentType.value = type
    }
}