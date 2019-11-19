package com.wbg.xigui.viewmodel

import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import log
import netDispatch

/**
 * @author xyx
 * @date :2019/7/9 17:18
 */
class MsgDetailViewModel : BaseViewModel() {
    fun setReaded(id: String) {
        val map = HashMap<String, Any>()
        map["id"] = id
        service.setReaded(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                log(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
            }
        })
    }
}