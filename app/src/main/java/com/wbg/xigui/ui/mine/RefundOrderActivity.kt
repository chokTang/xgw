package com.wbg.xigui.ui.mine

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.ReFundAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.RefundBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.RefundDetailActivity.Companion.KEY_BUNDLE
import com.wbg.xigui.ui.mine.RefundDetailActivity.Companion.KEY_ORDER_ID
import com.wbg.xigui.ui.mine.RefundDetailActivity.Companion.KEY_SKU_ID
import com.wbg.xigui.viewmodel.RefundOrderViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.refund_order_activity.*
import startRout

/**
 * @author xyx
 * @date :2019/7/10 18:04
 */
@Route(path = RoutUrl.Mine.order_refund)
class RefundOrderActivity : BaseXActivity<RefundOrderViewModel>() {

    var adapter: ReFundAdapter? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        setTitle("退款")
        addView(R.layout.refund_order_activity)
        val linearLayoutManager = LinearLayoutManager(this)
        rv_refund.layoutManager = linearLayoutManager
        adapter = ReFundAdapter()
        rv_refund.adapter = adapter
    }

    override fun initData() {

        loadViewHelper = (object : LoadViewHelper(this) {
            override fun action() {
                mViewModel.refresh {
                    adapter?.setNewData(it)
                }
            }
        })
        mViewModel.getData {
            adapter?.setNewData(it)
        }

        refresh_layout.setOnRefreshListener {
            mViewModel.refresh {
                adapter?.setNewData(it)
            }
        }

        refresh_layout.setOnLoadMoreListener {
            mViewModel.getData {
                adapter?.setNewData(it)
            }
        }

        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean  = adapter.data[position] as RefundBean
            val bundle = Bundle()
            bundle.putString(KEY_ORDER_ID,bean.orderId)
            bundle.putString(KEY_SKU_ID,bean.skuId)
            startRout(RoutUrl.Mine.refund_detail, KEY_BUNDLE,bundle)
        }
    }
}