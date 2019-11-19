package com.wbg.xlib.net

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.wbg.xigui.net.HeadInterceptor
import com.wbg.xigui.net.SSLSocketClient
import com.wbg.xigui.net.XGsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        const val host = "https://test.xiguiwang.com/"
        const val h5 = "http://test.xiguiwang.com:8087/"
        private val logger = LoggerInterceptor()
        private val header = HeadInterceptor()
        val client by lazy {
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(
                        10,
                        TimeUnit.SECONDS
                    ).writeTimeout(
                        10,
                        TimeUnit.SECONDS
                    ).sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(
                        SSLSocketClient.getHostnameVerifier()
                    ).addInterceptor(header).addInterceptor(logger).build()
                )
                .baseUrl(host)
                .addConverterFactory(XGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        val clientPic by lazy {
            Retrofit.Builder()
                .client(
                    OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(
                        60,
                        TimeUnit.SECONDS
                    ).writeTimeout(
                        60,
                        TimeUnit.SECONDS
                    ).sslSocketFactory(SSLSocketClient.getSSLSocketFactory()).hostnameVerifier(
                        SSLSocketClient.getHostnameVerifier()
                    ).addInterceptor(header).addInterceptor(logger).build()
                )
                .baseUrl(host)
                .addConverterFactory(XGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }

}