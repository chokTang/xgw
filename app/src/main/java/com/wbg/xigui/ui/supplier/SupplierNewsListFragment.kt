package com.wbg.xigui.ui.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.SupplierNewsListFragmentBinding
import com.wbg.xigui.viewmodel.SupplierNewsListViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.supplier_news_list_fragment.*

/**
 * @author xyx
 * @date :2019/7/16 10:51
 */
class SupplierNewsListFragment : BaseXFragment<SupplierNewsListViewModel>() {
    var binding: SupplierNewsListFragmentBinding? = null
    var type = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.get("type") as Int
    }

    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initData() {
        binding?.run {
            mViewModel.type = type
            loadViewHelper = (object : LoadViewHelper(context!!) {
                override fun action() {
                    model?.refresh()
                }
            })
            loadViewHelper?.showLoading(refreshLayout)
            model?.getData()
            refreshLayout.setOnRefreshListener {
                model?.refresh()
            }
            refreshLayout.setOnLoadMoreListener {
                model?.getData()
            }
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.supplier_news_list_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initImmersionBar() {
    }
}