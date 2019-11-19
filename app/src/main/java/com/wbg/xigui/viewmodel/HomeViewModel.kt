package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.base.BaseViewModel

class HomeViewModel : BaseViewModel() {
    val fragmentType = MutableLiveData<Int>()
    fun changeFragment(type: Int) {
        fragmentType.value = type
    }
    fun update(){

    }
}