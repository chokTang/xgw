package com.wbg.xlib.net

import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

class LoggerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val orgRequest = chain!!.request()
        val response = chain.proceed(orgRequest)
        val body = orgRequest.body()
//        val sb = StringBuilder()
        if (orgRequest.method() == "POST") {
            val body1 = body
//            for (i in 0 until body1.size()) {
//                sb.append(body1.encodedName(i) + "=" + body1.encodedValue(i) + ",")
//            }
//            sb.delete(sb.length - 1, sb.length)
            var buffer = Buffer()
            body1?.writeTo(buffer)
            var charset = Charset.forName("utf8")
            var bodyStr = buffer.readString(charset)
            //打印post请求的信息
            if (body is MultipartBody) {
                Log.e(
                    "==http==请求",
                    "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                            + "\n" + "headers:" + orgRequest.headers()
                            + "\n" + "上传文件"
                )
            } else {
                Log.e(
                    "==http==请求",
                    "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                            + "\n" + "headers:" + orgRequest.headers()
                            + "\n" + "post请求体:{" + bodyStr + "}"
                )
            }

        } else {
            //打印get请求的信息
            Log.e(
                "==http==请求",
                "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                        + "\n" + "headers:" + orgRequest.headers().toMultimap()
            )
        }
        //返回json
        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()
        var charset = Charset.forName("utf8")
        val contentType = responseBody.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(Charset.forName("utf8"))
            } catch (e: UnsupportedCharsetException) {
                return response
            }

        }
        if (contentLength != 0L) {
            //打印返回json
            //json日志使用鼠标中键进行选中
            Log.e(
                "==http==响应", "code=" + response.code() + "|method=" + orgRequest.method() + "|url=" + orgRequest.url()
                        + "\n" + buffer.clone().readString(charset)
            )
        }
        return response
    }

}