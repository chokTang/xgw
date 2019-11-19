package com.wbg.xigui.net

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.wbg.xigui.bean.ReturnModel
import com.wbg.xigui.bean.TempModel
import com.wbg.xigui.utils.AesEncryptUtils
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.nio.charset.Charset


/**
 * @author xyx
 * @date :2019/7/2 10:19
 */
class XGsonResponseBodyConverter<T> : Converter<ResponseBody, T> {
    private val tag by lazy {
        XGsonResponseBodyConverter::class.java.simpleName
    }
    private val gson: Gson
    private val adapter: TypeAdapter<T>

    constructor(gson: Gson, adapter: TypeAdapter<T>) {
        this.gson = gson
        this.adapter = adapter
    }

    @Throws(IOException::class)
    override fun convert(responseBody: ResponseBody): T {
        val contentLength = responseBody!!.contentLength()
        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()
        var charset = Charset.forName("utf8")
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(Charset.forName("utf8"))

        }
        var resultJson = ""
        if (contentLength != 0L) {
            val json = buffer.clone().readString(charset)
            val model = gson.fromJson<TempModel>(json, TempModel::class.java)
            if (model.status != 1000 && model.status != 401) {
                throw Exception(model.message)
            }
            var dataStr = if (model.data is String) {//返回加密的字符串
                AesEncryptUtils.aesDecrypt(model.data as String).apply {
                    Log.d(this@XGsonResponseBodyConverter.tag, this)
                    Log.e("请求的json数据为", this)

                }
            } else {//返回未加密的对象
                "object"
            }
            if (dataStr != "object" && !dataStr.startsWith("{") && !dataStr.startsWith("[")) {
                dataStr = "{}"
            }
            var returnModel: ReturnModel<Any>? = null
            if (dataStr == "object") {
                returnModel = ReturnModel(
                    model.message,
                    model.status,
                    model.error,
                    model.data
                )
            } else {
                if (dataStr.startsWith("{")) {//返回为 对象空数据的时候
                    returnModel = if (dataStr == "{}") {//空数据的时候
                        ReturnModel(
                            model.message,
                            model.status,
                            model.error,
                            null
                        )
                    } else {
                        ReturnModel(
                            model.message,
                            model.status,
                            model.error,
                            gson.fromJson(dataStr, JsonObject::class.java)
                        )
                    }

                } else {// 返回为[]  数组空数据的时候
                    returnModel = if (dataStr == "[]") {//空数据的时候
                        ReturnModel(
                            model.message,
                            model.status,
                            model.error,
                            null
                        )
                    } else {
                        ReturnModel(
                            model.message,
                            model.status,
                            model.error,
                            gson.fromJson(dataStr, JsonArray::class.java)
                        )
                    }
                }
            }

            resultJson = gson.toJson(returnModel)
        }
        responseBody.use {
            return adapter.fromJson(resultJson)
        }
    }

}