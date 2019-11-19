package com.wbg.xigui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.BaseBean
import com.wbg.xigui.bean.ReturnModel
import com.wbg.xigui.databinding.SupplierHomeActivityBinding
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.supplier.SupplierGoodsFragment
import com.wbg.xigui.ui.supplier.SupplierMineFragment
import com.wbg.xigui.ui.supplier.SupplierNewsFragment
import com.wbg.xigui.viewmodel.SupplierHomeViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.supplier_home_activity.*
import netDispatch

/**
 * @author xyx
 * @date :2019/7/11 9:32
 */
@Route(path = RoutUrl.Supplier.home)
class SupplierHomeActivity : BaseXActivity<SupplierHomeViewModel>() {
    private var currentFragment: Fragment? = null
    var binding: SupplierHomeActivityBinding? = null
    var tabs: Array<View>? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.supplier_home_activity)
        tabs = arrayOf(store_tv, msg_tv, mine_tv)
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.fragmentType.observe(this@SupplierHomeActivity, Observer {
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
                    currentFragment = SupplierGoodsFragment()
                }
                1 -> {
                    currentFragment = SupplierNewsFragment()
                }
                2 -> {
                    currentFragment = SupplierMineFragment()
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