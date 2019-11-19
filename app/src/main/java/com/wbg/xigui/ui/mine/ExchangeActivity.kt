package com.wbg.xigui.ui.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.RightsExchaneActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.ExchangeViewModel
import toast

/**
 * @author xyx
 * @date :2019/6/27 11:19
 */
@Route(path = RoutUrl.Mine.exchange)
class ExchangeActivity : BaseXActivity<ExchangeViewModel>() {
    var binding: RightsExchaneActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("债权兑换")
        setRightColor(ResourcesUtil.getColor(R.color.text_black))
        setRightText("兑换规则")
        setRightAction {
            toast("跳网页")
        }
        binding = bindLayout(R.layout.rights_exchane_activity)
    }

    override fun initData() {
        binding?.run {
            mViewModel.context = this@ExchangeActivity
            model = mViewModel
            mViewModel.getData()
        }
    }
}