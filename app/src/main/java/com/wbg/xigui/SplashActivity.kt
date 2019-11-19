package com.wbg.xigui

import android.os.Handler
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.rout.RoutUrl
import kotlinx.android.synthetic.main.splash_activity.*
import otherwise
import startRout
import yes

/**
 * @author xyx
 * @date :2019/7/12 10:46
 */
class SplashActivity : BaseXActivity<BaseViewModel>() {
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        addView(R.layout.splash_activity)
        ImmersionBar.with(this).reset().titleBar(bg).init()
    }

    override fun initData() {
        Handler().postDelayed({
            go()
            finish()
        }, 1000)

    }

    private fun go() {
        XApplication.isLogin().yes {
            when (XApplication.instance.getUserInfo().currentRole) {
                RoleType.creditor -> {
                    startRout(RoutUrl.Main.home_activity)
                }
                RoleType.debtor -> {
                    startRout(RoutUrl.Debtor.home)
                }
                RoleType.supplier -> {
                    startRout(RoutUrl.Supplier.home)
                }
                RoleType.agent -> {
                    startRout(RoutUrl.Agent.home)
                }
            }
        }.otherwise {
            startRout(RoutUrl.Main.home_activity)
        }
    }
}