package com.wbg.xigui.utils

import com.wbg.xigui.bean.FileBody
import com.wbg.xigui.bean.UpLoadBean
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.servicePic
import netDispatch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import toast
import java.io.File

/**
 * @author xyx
 * @date :2019/7/8 11:37
 */
class UploadUtil {
    companion object {
        /**
         * 上传类型：0-头像，1-债权，2-评论，3-退货，99-其他
         */
        fun uploadFile(type: Int, filePath: String, observer: RxNetObserver<UpLoadBean>) {
            val file = File(filePath)
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            val body = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            builder.addFormDataPart("file", filePath, body)
            builder.addFormDataPart("uploadType", type.toString())
            val parts = builder.build().parts()
            servicePic.uploadFile(parts).netDispatch(observer)
        }


        /**
         * 上传文件  正在上传图片……
         * 上传类型：0-头像，1-债权，2-评论，3-退货，99-其他
         */
        fun uploadFileBody(
            list: MutableList<FileBody>,
            type: Int,
            hint: String,
            block: (list: MutableList<UpLoadBean>?) -> Unit
        ) {
            val uploadList: MutableList<UpLoadBean> = arrayListOf() //上传成功的文件
            for (i in 0 until list.size) {
                UploadUtil.uploadFile(type,
                    list[i].file!!, object : RxNetObserver<UpLoadBean>(true, hint, false) {
                        override fun onError(msg: String) {
                            toast(msg)
                        }

                        override fun onStart() {

                        }

                        override fun onSuccess(t: UpLoadBean?) {
                            uploadList.add(t!!)
                            if (list.size == uploadList.size) {
                                block(uploadList)
                            }
                        }
                    })
            }

        }
    }
}