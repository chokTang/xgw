package com.wbg.xigui.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.CommentListBinding
import com.wbg.xigui.viewmodel.CommentListViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.comment_list.*

/**
 * @author tyk
 * @date :2019/8/14 17:55
 * @des : 评论列表
 */
class CommentListFragment : BaseXFragment<CommentListViewModel>() {
    var id :String?= ""
    var binding: CommentListBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            loadViewHelper = (object : LoadViewHelper(context!!) {
                override fun action() {
                    model?.refresh()
                }
            })
            loadViewHelper?.showLoading(refreshLayout)
            model?.getData()
            refreshLayout.setOnRefreshListener { mViewModel.refresh() }
            refreshLayout.setOnLoadMoreListener { mViewModel.getData() }
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewModel.id = id!!
        binding = bindLayout(inflater, R.layout.comment_list, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initImmersionBar() {
    }
}