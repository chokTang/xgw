package com.wbg.xigui.viewmodel

import androidx.databinding.ObservableField
import com.wbg.xigui.base.BaseViewModel

class TestFragmentModel : BaseViewModel() {
    val url = ObservableField<String>()
}