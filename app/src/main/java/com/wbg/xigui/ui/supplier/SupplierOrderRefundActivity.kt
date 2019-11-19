package com.wbg.xigui.ui.supplier

import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.SupplierOrderRefundActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.SupplierOrderRefundViewModel
import kotlinx.android.synthetic.main.supplier_order_refund_activity.*

/**
 * @author xyx
 * @date :2019/7/16 14:51
 */
@Route(path = RoutUrl.Supplier.order_refund)
class SupplierOrderRefundActivity : BaseXActivity<SupplierOrderRefundViewModel>() {
    var binding: SupplierOrderRefundActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.supplier_order_refund_activity)
    }

    override fun initData() {
        ImmersionBar.with(this).reset()
        ImmersionBar.with(this).titleBar(top_title_fl).init()
        binding?.run {
            model = mViewModel
            mViewModel.getData()
        }
    }
}