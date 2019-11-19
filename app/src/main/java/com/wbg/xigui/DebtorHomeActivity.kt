package com.wbg.xigui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.DebtorHomeActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.DebtorHomeViewModel
import com.wbg.xigui.ui.debtor.DebtorMineFragment
import com.wbg.xigui.ui.debtor.DebtorRepayFragment
import kotlinx.android.synthetic.main.debtor_home_activity.*

/**
 * @author xyx
 * @date :2019/7/12 10:13
 */
@Route(path = RoutUrl.Debtor.home)
class DebtorHomeActivity:BaseXActivity<DebtorHomeViewModel>() {
    private var currentFragment: Fragment? = null
    var binding: DebtorHomeActivityBinding? = null
    var tabs: Array<View>? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.debtor_home_activity)
        tabs = arrayOf(repay_tv, mine_tv)
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.fragmentType.observe(this@DebtorHomeActivity, Observer {
                replaceFragment(it)
            })
            replaceFragment(0)
        }
    }

    fun setSelectedView(index: Int) {
        tabs?.run {
            forEach { it.isSelected = false }
            this[index].isSelected = true
        }

    }

    private fun replaceFragment(type: Int) {
        setSelectedView(type)
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag("flag$type")
        var bundle = Bundle()
        bundle.putInt("type", type)
        if (currentFragment == null) {
            when (type) {
                0 -> {
                    currentFragment = DebtorRepayFragment()
                }
                1 -> {
                    currentFragment = DebtorMineFragment()
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