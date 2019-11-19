package com.wbg.xigui.viewmodel

import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.FlagBean
import com.wbg.xigui.bean.body.AddCommentBody
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.utils.PamasUtil
import netDispatch

/**
 * @author tyk
 * @date :2019/8/26 10:00
 * @des :
 */
class AddCommentViewModel : BaseViewModel() {


    /**
     * 添加评论（写评论）
     */
    fun addComment(body: AddCommentBody?, block: (bean: FlagBean?) -> Unit) {
        service.addComment(PamasUtil.objectToMap(body) as HashMap<String, @JvmSuppressWildcards Any>)
            .netDispatch(object : RxNetObserver<FlagBean>() {
                override fun onError(msg: String) {
                    errorMsg.value = msg
                }

                override fun onStart() {
                }

                override fun onSuccess(t: FlagBean?) {
                    block(t)
                }
            })
    }


}