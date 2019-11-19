package com.wbg.xigui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.UpLoadBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.ui.mine.SettingActivity
import com.wbg.xigui.utils.UploadUtil
import netDispatch
import toast

/**
 * @author xyx
 * @date :2019/7/5 11:41
 */
class PersonInfoViewModel : BaseViewModel() {
    var imgUrl = MutableLiveData<String>()
    var nickName = MutableLiveData<String>()
    var avatarId = ""
    fun setInfo() {
        val map = HashMap<String, Any>()
        map["avatarId"] = avatarId
        map["nickName"] = nickName.value ?: ""
        service.updateUserInfo(map).netDispatch(object : RxNetObserver<Any>(true, "正在修改用户信息……") {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: Any?) {
                XApplication.instance.finishActivity(SettingActivity::class.java)
                XApplication.currentActivity.finish()
                toast("保存成功")
            }
        })
    }

    fun uploadImg(path: String) {
        UploadUtil.uploadFile(0, path, object : RxNetObserver<UpLoadBean>(true, "正在上传头像……", false) {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: UpLoadBean?) {
                t?.run {
                    avatarId = imgId ?: ""
                }
                imgUrl.value = t?.imgUrl
            }
        })
    }
}