package com.wbg.xigui.ui.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.SupplierNewsFragmentBinding
import com.wbg.xigui.viewmodel.SupplierNewsViewModel
import kotlinx.android.synthetic.main.supplier_news_fragment.*

/**
 * @author xyx
 * @date :2019/7/16 10:17
 */
class SupplierNewsFragment : BaseXFragment<SupplierNewsViewModel>() {
    private var currentFragment: Fragment? = null
    var binding: SupplierNewsFragmentBinding? = null
    var tabs: Array<View>? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        tabs = arrayOf(all_tv, order_tv, other_tv)
        binding?.run {
            model = mViewModel
            mViewModel.fragmentType.observe(this@SupplierNewsFragment, Observer {
                replaceFragment(it)
            })
            replaceFragment(0)
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.supplier_news_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this).titleBar(top_title_fl).init()
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
            childFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = childFragmentManager.findFragmentByTag("flag$type")
        var bundle = Bundle()
        bundle.putInt("type", type)
        if (currentFragment == null) {
            currentFragment = SupplierNewsListFragment()
            currentFragment!!.arguments = bundle
            childFragmentManager.beginTransaction().add(R.id.content, currentFragment!!, "flag$type").commit()
            childFragmentManager.beginTransaction().hide(currentFragment!!).commit()//先hide一次  触发懒加载逻辑
            childFragmentManager.beginTransaction().show(currentFragment!!).commit()
        } else {
            childFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }
}