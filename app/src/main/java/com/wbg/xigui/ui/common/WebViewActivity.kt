package com.wbg.xigui.ui.common

import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.widget.ProgressWebView
import kotlinx.android.synthetic.main.webview_activity.*

/**
 * @author xyx
 * @date :2019/7/3 15:56
 */
@Route(path = RoutUrl.Common.web_view)
class WebViewActivity : BaseXActivity<BaseViewModel>(), ProgressWebView.OnTitleListener {
    companion object{
        const val KEY_LOAD_URL =  "loadUrl"
    }
    private var loadUrl = "about:blank"
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        addView(R.layout.webview_activity)
    }

    override fun initData() {
        loadUrl = intent.getStringExtra(KEY_LOAD_URL)
        webview.loadUrl(loadUrl)
        webview.setOnTitleListener(this)
    }

    override fun onDestroy() {
        webview.setOnTitleListener(null)
        webview.destroy()
        super.onDestroy()
    }

    override fun onTitleChanged(title: String?) {
        setTitle(title)
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}