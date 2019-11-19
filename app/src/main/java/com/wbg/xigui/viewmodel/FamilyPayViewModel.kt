package com.wbg.xigui.viewmodel

import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.AvatarBean
import com.wbg.xigui.bean.RecordBean
import com.wbg.xigui.bean.RecordBeanList
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.widget.ObservableReplaceArrayList
import netDispatch

/**
 * @author tyk
 * @date :2019/9/5 9:58
 * @des :
 */

class FamilyPayViewModel : BaseViewModel() {

    val list = ObservableReplaceArrayList<RecordBeanList>()

    /**
     * 获取亲情付成员列表
     */
    fun getMenberList(block:(bean:AvatarBean)->Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.getFamilyList(map).netDispatch(object : RxNetObserver<AvatarBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: AvatarBean?) {
                block(t!!)
            }
        })

    }


    /**
     * 添加亲情付成员
     */
    fun addMember(phone:String,block:(bean:Any)->Unit) {
        val map = HashMap<String, Any>()
        map["phone"] = phone
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.addFamilyList(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
                block(t!!)
            }
        })

    }


    /**
     * 删除亲情付成员
     */
    fun deleteMember(id:String,block:(bean:Any)->Unit) {
        val map = HashMap<String, Any>()
        map["shareUserId"] = id
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.deleteFamilyList(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
                block(t!!)
            }
        })

    }

    /**
     * 获取支付记录
     */
    fun getRecordList(block: (bean: MutableList<RecordBeanList>) -> Unit) {
        val map = HashMap<String, Any>()
        map["memberId"] = XApplication.instance.getMemberId().toString()
        map["page"] = mPage
        map["size"] = 20
        service.getRecordList(map).netDispatch(object : RxNetObserver<RecordBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
                changeLoadType(list, null, false)
            }

            override fun onStart() {
            }

            override fun onSuccess(t:RecordBean?) {
                mPage++
                list.addAll(t?.list!!)
                if (t.list.isEmpty()){
                    changeLoadType(list, null, false)
                }else{
                    changeLoadType(list, listOf(t.list), false)
                }
                block(list)
            }
        })

    }


    fun refresh(block: (bean: MutableList<RecordBeanList>) -> Unit) {
        list.clear()
        mPage = 1
        getRecordList(block)
    }
}