package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.base.BaseViewModel

/**
 * @author xyx
 * @date :2019/7/12 10:04
 */
class AgentHomeViewModel :BaseViewModel(){
    val fragmentType = MutableLiveData<Int>()
    fun changeFragment(type: Int) {
        fragmentType.value = type
    }
}