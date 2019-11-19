package com.wbg.xigui.net

import com.google.gson.Gson
import com.wbg.xigui.XApplication
import com.wbg.xigui.utils.AesEncryptUtils
import java.util.*
import kotlin.collections.HashMap

fun HashMap<*, *>.getParam(): HashMap<String, Any> {
    return this as HashMap<String, Any>
}

fun HashMap<*, *>.getRequestParam(): HashMap<String, Any> {
    val headMap = HashMap<String, Any>()
    val map = HashMap<String, Any>()
    headMap["channel"] = "android"
    headMap["token"] = XApplication.instance.getToken() ?: ""
    headMap["uuid"] = UUID.randomUUID().toString().replace("-", "")
    map["head"] = headMap
    map["data"] = AesEncryptUtils.aesEncrypt(Gson().toJson(this))
    return map
}