package com.wbg.xigui.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXFragment
import kotlinx.android.synthetic.main.webview_activity.*

/**
 * @author TYk
 * @date :2019/7/3 15:56
 */
class WebViewFragment : BaseXFragment<BaseViewModel>() {


     var loadUrl = "about:blank"
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        webview.loadUrl(loadUrl)
    }


    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addView(inflater,R.layout.webview_activity,container)
        return mRootView
    }

    override fun initImmersionBar() {
    }

    override fun onDestroy() {
        if (null!=webview){
            webview.destroy()
        }
        super.onDestroy()
    }


}