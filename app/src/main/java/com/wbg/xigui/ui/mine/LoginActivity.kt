package com.wbg.xigui.ui.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import kotlinx.android.synthetic.main.common_layout.*

/**
 * @author xyx
 * @date :2019/6/24 14:13
 * @des :登录页面
 */
@Route(path = RoutUrl.Common.login)
class LoginActivity : BaseXActivity<BaseViewModel>() {
    private var currentFragment: Fragment? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        addView(R.layout.login_activity)
    }

    override fun initData() {
        setTitle("")
        replaceFragment(0)
        setRightText("密码登录")
        setRightAction {
            if (right_tv.text.toString() == "密码登录") {
                replaceFragment(1)
                setRightText("验证码登录")
            } else {
                replaceFragment(0)
                setRightText("密码登录")
            }
        }
    }

    private fun replaceFragment(type: Int) {
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag("flag$type")
        var bundle = Bundle()
        if (currentFragment == null) {
            when (type) {
                0 -> {
                    currentFragment = CodeLoginFragment()
                }
                1 -> {
                    currentFragment = PwdLoginFragment()
                }
            }
            currentFragment!!.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.content, currentFragment!!, "flag$type").commit()
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()//先hide一次  触发懒加载逻辑
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }
}