package com.wbg.xigui.viewmodel

import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.BankBean
import com.wbg.xigui.bean.ListBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.utils.StrUtil
import netDispatch
import toast

/**
 * @author tyk
 * @date :2019/9/6 14:29
 * @des :
 */
class BankCardManageViewModel : BaseViewModel(){

    /**
     * 获取银行卡列表
     */
    fun getBankList(block: (list: ArrayList<BankBean>) -> Unit) {
        service.getBankList(HashMap()).netDispatch(object : RxNetObserver<ListBean<BankBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: ListBean<BankBean>?) {
                t?.run {
                    block(list)
                }
            }
        })
    }


    /**
     * 添加银行卡
     */
    fun bindBank(bankCard: String, idCard: String, mobile: String, realName: String, block: (bankList: ArrayList<BankBean>) -> Unit) {
        if (bankCard.isNullOrEmpty()) {
            toast("请填写银行卡号")
            return
        }
        if (!StrUtil.isIDCard(idCard)) {
            toast("请填写正确的身份证号")
            return
        }
        if (!StrUtil.isMobileNo(mobile)) {
            toast("请填写正确的手机号")
            return
        }
        if (realName.isNullOrEmpty()) {
            toast("请填写姓名")
            return
        }
        val map = HashMap<String, Any>()
        map["bankCard"] = bankCard
        map["idCard"] = idCard
        map["mobile"] = mobile
        map["realName"] = realName
        service.bindBank(map).netDispatch(object : RxNetObserver<ListBean<BankBean>>(true) {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: ListBean<BankBean>?) {
                t?.run {
                    block(list)
                }
            }
        })
    }
}