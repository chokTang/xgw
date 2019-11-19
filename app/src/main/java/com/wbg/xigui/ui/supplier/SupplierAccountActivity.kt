package com.wbg.xigui.ui.supplier

import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.SupplierAccountActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.SupplierAccountViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.supplier_account_activity.*

/**
 * @author xyx
 * @date :2019/7/16 9:41
 */
@Route(path = RoutUrl.Supplier.account, extras = RoutUrl.Extra.login)
class SupplierAccountActivity : BaseXActivity<SupplierAccountViewModel>() {
    var binding: SupplierAccountActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.supplier_account_activity)
        ImmersionBar.with(this).reset().titleBar(top_title_fl).init()
        back_area.setOnClickListener { onBackPressed() }
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.bean.observe(this@SupplierAccountActivity, Observer {
                it?.run {
                    all_money_tv.text = "￥$bond"
                    balance_available_tv.text = "￥$availableBalance"
                    withdrawal_balance_tv.text = "￥$withdrawalBalanece"
                    balance_available_ps_tv.text = "含可用余额：￥$availableBalance"
                }
            })
            loadViewHelper = (object : LoadViewHelper(this@SupplierAccountActivity) {
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
                model?.loadMore()
            }
        }
    }
}