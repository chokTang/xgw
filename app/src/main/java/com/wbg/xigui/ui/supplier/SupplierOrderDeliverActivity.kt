package com.wbg.xigui.ui.supplier

import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.SupplierOrderDeliverViewModel
import kotlinx.android.synthetic.main.supplier_oder_not_delivery_activity.*

/**
 * @author xyx
 * @date :2019/7/16 14:30
 */
@Route(path = RoutUrl.Supplier.order_deliver)
class SupplierOrderDeliverActivity : BaseXActivity<SupplierOrderDeliverViewModel>() {
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        addView(R.layout.supplier_oder_not_delivery_activity)
    }

    override fun initData() {
        ImmersionBar.with(this).reset()
        ImmersionBar.with(this).titleBar(top_title_fl).init()
    }
}