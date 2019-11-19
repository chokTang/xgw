package com.wbg.xigui.ui.agent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXFragment

/**
 * @author xyx
 * @date :2019/7/17 11:16
 *  @des ：代理商  （主页）
 */
class AgentEarningsFragment : BaseXFragment<BaseViewModel>() {
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addView(inflater, R.layout.agent_earnings_fragment, container)
        return mRootView
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
    }
}