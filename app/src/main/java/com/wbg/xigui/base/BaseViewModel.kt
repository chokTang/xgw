package com.wbg.xigui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wbg.xigui.utils.LoadingType
import otherwise
import yes

open class BaseViewModel : ViewModel() {
    var mPage = 1
    val loadType = MutableLiveData<LoadingType>()
    val errorMsg = MutableLiveData<String>()
    fun changeLoadType(pageList: List<Any>, data: List<Any>?, isError: Boolean) {
        isError.yes {
            pageList.isEmpty().yes { loadType.value = LoadingType.ERROR_EMPTY }
                .otherwise { loadType.value = LoadingType.ERROR_NOT_EMPTY }
        }
            .otherwise {
                (data?.size ?: 0 == 0).yes {
                    pageList.isEmpty().yes { loadType.value = LoadingType.EMPTY }.otherwise {
                        if (mPage == 1)
                            loadType.value = LoadingType.EMPTY
                        else
                            loadType.value = LoadingType.NO_MORE
                    }
                }
                    .otherwise {
                        loadType.value = LoadingType.SUCCESS
                    }
            }
    }

    open fun onCreate() {

    }

    open fun onDestroy() {

    }
}


