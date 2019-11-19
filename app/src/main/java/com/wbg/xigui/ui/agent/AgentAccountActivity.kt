package com.wbg.xigui.ui.agent

import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.AgentAccountActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.AgentAccountViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.agent_account_activity.*

/**
 * @author xyx
 * @date :2019/7/17 10:50
 */
@Route(path = RoutUrl.Agent.account)
class AgentAccountActivity : BaseXActivity<AgentAccountViewModel>() {
    var binding: AgentAccountActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.agent_account_activity)
        ImmersionBar.with(this).reset().titleBar(top_title_fl).init()
        back_area.setOnClickListener { onBackPressed() }
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.bean.observe(this@AgentAccountActivity, Observer {
                it?.run {
                    balance_tv.text = "￥$balance"
                    balance_available_tv.text = "￥$availableBalance"
                    balance_unavailable_tv.text = "￥${balance - availableBalance}"
                    withdrawal_balance_tv.text = "￥$withdrawalBalanece"
                    all_earning_tv.text = "￥$bond"
                }
            })
            loadViewHelper = (object : LoadViewHelper(this@AgentAccountActivity) {
                override fun action() {
                    model?.refresh()
                }
            })
            loadViewHelper?.showLoading(refresh_layout)
            model?.getData()
            refresh_layout.setOnRefreshListener {
                model?.refresh()
            }
            refresh_layout.setOnLoadMoreListener {
                model?.loadMore()
            }
        }
    }

    fun showPs(view: View) {
        var dialog = AlertDialog.Builder(this)
            .setTitle((view as TextView).text.toString())
        dialog.setMessage(
            when ((view as TextView).text.toString()) {
                "可用余额" -> "可用余额是指用户本人账户中可以使用的资金，可以提现到指定的银行卡中。"
                "不可用余额" -> "不可用余额是指用户本人账户中暂时不能使用的那部分资金，如购买的商品未确认收货时兑换的资金。"
                else -> "提现中用户本人发起提现申请后，未到到账的那部分资金"
            }
        )
        dialog.show()
    }
}