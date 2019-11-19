package com.wbg.xigui.ui.debtor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.DebtorRepayFragmentBinding
import com.wbg.xigui.viewmodel.DebtorRepayViewModel

/**
 * @author xyx
 * @date :2019/7/18 10:14
 */
class DebtorRepayFragment : BaseXFragment<DebtorRepayViewModel>() {
    var binding: DebtorRepayFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mViewModel.getUnReadNum()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getUnReadNum()
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.debtor_repay_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return  mRootView
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
    }
}