package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ShareCodeBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import netDispatch

/**
 * @author tyk
 * @date :2019/9/6 16:55
 * @des :
 */
class ShareViewModel : BaseViewModel(){
    /**
     *  获取分享码
     */
    fun getShareCode(type:String,block:(bean: ShareCodeBean)->Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId().toString()
        map["type"] =type
        service.getShareCode(map).netDispatch(object : RxNetObserver<ShareCodeBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: ShareCodeBean?) {
                block(t!!)
            }
        })

    }
}