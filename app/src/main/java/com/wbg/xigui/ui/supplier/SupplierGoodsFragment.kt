package com.wbg.xigui.ui.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.SupplierGoodsFragmentBinding
import com.wbg.xigui.viewmodel.SupplierGoodsViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.supplier_goods_fragment.*

/**
 * @author xyx
 * @date :2019/7/16 11:19
 */
class SupplierGoodsFragment : BaseXFragment<SupplierGoodsViewModel>() {
    var binding: SupplierGoodsFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initData() {
        loadMore = false
        binding?.run {
            loadViewHelper = (object : LoadViewHelper(context!!) {
                override fun action() {
                    model?.refresh()
                }
            })
            loadViewHelper?.showLoading(refreshLayout)
            model?.refresh()
            refreshLayout.isEnableLoadMore = false
            refreshLayout.setOnRefreshListener {
                model?.refresh()
            }
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.supplier_goods_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).titleBar(top_title_fl).init()
    }
}