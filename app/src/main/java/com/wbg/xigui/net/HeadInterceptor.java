package com.wbg.xigui.net;

import android.text.TextUtils;
import android.util.Log;
import com.wbg.xigui.BuildConfig;
import com.wbg.xigui.XApplication;
import com.wbg.xigui.utils.AesEncryptUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * <p>
 * 作者：jakee
 * 创建时间：2019/4/29
 */
public class HeadInterceptor implements Interceptor {
    private static final String TAG = "HeadInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        try {
            addHttpHeader(chain, builder);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("==请求头添加错误==", e.getMessage());
            }
        }
        return chain.proceed(builder.build());
    }

    private void addHttpHeader(Chain chain, Request.Builder builder) {
        builder.addHeader("Content-type", "application/json; charset=UTF-8");
        builder.addHeader("clt", "Android");
        builder.addHeader("device", android.os.Build.MODEL);
        String time = System.currentTimeMillis() + "";
        builder.addHeader("timestamp", time);
        try {
            builder.addHeader("sign", AesEncryptUtils.aesEncrypt(time + AesEncryptUtils.AESKEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String role =XApplication.instance.getRole()!=null?XApplication.instance.getRole():XApplication.instance.getUserInfo().getCurrentRole();
        if (TextUtils.isEmpty(role)) {
            role = "creditor";
        }
        builder.addHeader("role", role);
    }
}
