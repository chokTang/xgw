package com.wbg.xigui.ui.near

import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.CommentListBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.CommentListViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.comment_list.*

/**
 * @author xyx
 * @date :2019/6/19 16:46
 */
@Route(path = RoutUrl.Near.comment_list)
class CommentListActivity : BaseXActivity<CommentListViewModel>() {
    companion object{
        const val KEY_PRODUCT_ID = "id"
    }
    var binding: CommentListBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        setTitle("评论")
        mViewModel.id = intent.getStringExtra(KEY_PRODUCT_ID)
        binding = bindLayout(R.layout.comment_list)
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            loadViewHelper = (object : LoadViewHelper(this@CommentListActivity) {
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
}