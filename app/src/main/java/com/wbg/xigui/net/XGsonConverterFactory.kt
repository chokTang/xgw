package com.wbg.xigui.net

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * @author xyx
 * @date :2019/7/2 9:34
 */
class XGsonConverterFactory : Converter.Factory {
    private lateinit var gson: Gson

    companion object {
        fun create(): XGsonConverterFactory {
            return XGsonConverterFactory(Gson())
        }
    }

    constructor(gson: Gson) {
        this.gson = gson
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return XGsonResponseBodyConverter(gson, adapter)

    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return XGsonRequestBodyConverter(gson, adapter)
    }
}