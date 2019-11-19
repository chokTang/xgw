package com.wbg.xigui.net

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import com.wbg.xigui.XApplication
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter

import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset
import java.util.HashMap

/**
 * @author xyx
 * @date :2019/7/2 11:13
 */
class XGsonRequestBodyConverter<T> internal constructor(private val gson: Gson, private val adapter: TypeAdapter<T>) :
    Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        if (value is HashMap<*, *>) {
            (value as HashMap<String, Any>)["memberId"] =XApplication?.instance.getMemberId()?:XApplication.instance.getUserInfo().member?.id ?: ""

            adapter.write(jsonWriter, (value.getRequestParam() as T))
        } else {
            adapter.write(jsonWriter, value)
        }
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }

    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }
}
