package com.wbg.xigui.ui.mine

import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.AccountActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.AccountViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.account_activity.*

/**
 * @author xyx
 * @date :2019/7/17 16:42
 * @des:我的账户详情
 */
@Route(path = RoutUrl.Mine.account_info, extras = RoutUrl.Extra.login)
class AccountActivity : BaseXActivity<AccountViewModel>() {
    companion object{
        const val KEY_POSITION = "position" // 进入页面默认是"收益" 页面或者是债权页面key
    }
    var binding: AccountActivityBinding? = null
    var position = 0
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.account_activity)
        ImmersionBar.with(this).reset().titleBar(top_title_fl).init()
        position = intent.getIntExtra(KEY_POSITION,0)
        if (position==0){
            earning_tv.isSelected = true
            rights_tv.isSelected = false
            rights_ll.visibility = View.GONE
            earning_ll.visibility = View.VISIBLE
        }else{
            earning_tv.isSelected = false
            rights_tv.isSelected = true
            earning_ll.visibility = View.GONE
            rights_ll.visibility = View.VISIBLE
        }

        back_area.setOnClickListener { onBackPressed() }
        earning_tv.setOnClickListener {
            earning_tv.isSelected = true
            rights_tv.isSelected = false
            rights_ll.visibility = View.GONE
            earning_ll.visibility = View.VISIBLE
        }
        rights_tv.setOnClickListener {
            earning_tv.isSelected = false
            rights_tv.isSelected = true
            earning_ll.visibility = View.GONE
            rights_ll.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.bean.observe(this@AccountActivity, Observer {
                it?.run {
                    balance_tv.text = "￥$balance"
                    balance_available_tv.text = "￥$availableBalance"
                    balance_unavailable_tv.text = "￥${balance - availableBalance}"
                    withdrawal_balance_tv.text = "￥$withdrawalBalanece"
                    consumption_count_tv.text = "$consumptionCount"
                    highest_amount_tv.text = "￥$highestAmount"
                    total_amount_tv.text = "￥$totalAmount"
                    right_balance_tv.text = "￥$surplusBond"
                }
            })
            loadViewHelper = (object : LoadViewHelper(this@AccountActivity) {
                override fun action() {
                    model?.refresh()
                }
            })
            model?.getData()
            refresh_layout.setOnRefreshListener {
                model?.refresh()
                refresh_layout.finishRefresh()
            }
            refresh_layout.setOnLoadMoreListener {
                model?.loadMore()
                refresh_layout.finishLoadMore()
            }
        }
    }

    /**
     * 提示
     */
    fun showPs(view: View) {
        var dialog = AlertDialog.Builder(this)
                .setTitle((view as TextView).text.toString())
        dialog.setMessage(
                when ((view as TextView).text.toString()) {
                    "可用余额" -> "可用余额是指用户本人账户中可以使用的资金，可以提现到指定的银行卡中。"
                    "不可用余额" -> "不可用余额是指用户本人账户中暂时不能使用的那部分资金，如购买的商品未确认收货时兑换的资金。"
                    else -> "提现中用户本人发起提现申请后，未到账的那部分资金"
                }
        )
        dialog.show()
    }
}