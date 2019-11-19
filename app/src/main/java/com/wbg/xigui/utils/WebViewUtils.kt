package com.wbg.xigui.utils

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * @author tyk
 * @date :2019/9/4 17:20
 * @des :
 */
class WebViewUtils {
    companion object{
        @SuppressLint("SetJavaScriptEnabled")
        fun webviewSet( mWebView:WebView) {
            /**获取webview设置 */
            val webSettings = mWebView.settings
            /**适配 */
            webSettings.useWideViewPort = true
            /**允许访问文件  */
            webSettings.setAllowFileAccess(true)
            webSettings.loadWithOverviewMode = true

            webSettings.loadWithOverviewMode = true
            /**设置 webview agent */
            webSettings.userAgentString = mWebView.settings.userAgentString + "jbxmall"
            /**支持js */
            webSettings.javaScriptEnabled = true
            /**设置缓存模式 */
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT
            /**开启 DOM缓存功能 */
            webSettings.domStorageEnabled = true
            /**关闭 Application Caches 缓存功能 */
            webSettings.setAppCacheEnabled(false)


            webSettings.builtInZoomControls = false
            /**http https地址图片连接 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

}