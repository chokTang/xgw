package com.wbg.xigui.ui.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.MsgActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.MsgViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.msg_activity.*

/**
 * @author xyx
 * @date :2019/6/27 11:38
 */
@Route(path = RoutUrl.Main.msg, extras = RoutUrl.Extra.login)
class MsgActivity : BaseXActivity<MsgViewModel>() {
    var binding: MsgActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        setTitle("消息中心")
        binding = bindLayout(R.layout.msg_activity)
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.context = this@MsgActivity
            loadViewHelper = (object : LoadViewHelper(this@MsgActivity) {
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
}