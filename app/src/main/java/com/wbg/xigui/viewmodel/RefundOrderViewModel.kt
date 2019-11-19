package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.RefundBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.widget.ObservableReplaceArrayList
import netDispatch

/**
 * @author xyx
 * @date :2019/7/10 17:58
 */
class RefundOrderViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<RefundBean>()

    /**
     * 获取退款退货列表
     */
    fun getData(block:(list: List<RefundBean>?)->Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId()!!
        map["page"] = mPage
        map["size"] = 20
        service.getRefundList(map).netDispatch(object : RxNetObserver<List<RefundBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
                changeLoadType(list, null, true)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<RefundBean>?) {
                mPage++
                t?.run {
                    list.addAll(this)
                }
                block(list)
                changeLoadType(list, t, false)
            }
        })
    }


    fun refresh(block:(list: List<RefundBean>?)->Unit) {
        list.clear()
        mPage = 1
        getData(block)
    }
}