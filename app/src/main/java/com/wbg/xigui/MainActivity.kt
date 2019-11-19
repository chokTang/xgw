package com.wbg.xigui

import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.ActivityMainBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.test.TestViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import toast

@Route(path = RoutUrl.Test.test_activity)
class MainActivity : BaseXActivity<TestViewModel>() {

    var binding: ActivityMainBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return swipe_layout
    }

    override fun initView() {
        binding = bindLayout(R.layout.activity_main)
        ImmersionBar.with(this).reset().init()
    }

    override fun initData() {
        XApplication.instance.toast("请先登录")
        binding?.run {
            model = mViewModel
            loadViewHelper = (object : LoadViewHelper(this@MainActivity) {
                override fun action() {
                    model?.refresh()
                }
            })
            loadViewHelper?.showLoading(swipeLayout)
            model?.getData()
            swipeLayout.setOnRefreshListener {
                model?.refresh()
            }
            swipeLayout.setOnLoadMoreListener {
                model?.getData()
            }
        }
    }


}
