package com.wbg.xigui.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.OrderAdapter
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.bean.ProductBean
import com.wbg.xigui.dialog.AlertDialogFragment
import com.wbg.xigui.dialog.PayDialog
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.comment.AddCommentActivity.Companion.KEY_BUNDLE
import com.wbg.xigui.ui.comment.AddCommentActivity.Companion.KEY_OREDR_BEAN
import com.wbg.xigui.ui.mine.OrderDetailActivity.Companion.KEY_ORDER_ID
import com.wbg.xigui.ui.mine.OrderDetailActivity.Companion.KEY_PRODUCT_ID
import com.wbg.xigui.ui.order.CreateOrderActivity
import com.wbg.xigui.ui.product.ProductDetailActivity.Companion.KEY_SKU_ID
import com.wbg.xigui.viewmodel.OrderFragmentViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.order_fragment.*
import startRout
import toast

/**
 * @author tyk
 * @date :2019/7/5 16:21
 */
class OrderFragment : BaseXFragment<OrderFragmentViewModel>() {
    var type = 0//0全部1待付款2待收货3已完成
    var adapter: OrderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.get("type") as Int
    }

    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initData() {

        val linearLayoutManager = LinearLayoutManager(activity)
        rv_order.layoutManager = linearLayoutManager
        adapter = OrderAdapter()
        rv_order.adapter = adapter

        mViewModel.type = type
        loadViewHelper = (object : LoadViewHelper(context!!) {
            override fun action() {
                mViewModel.refresh {
                    adapter?.setNewData(it)
                }
            }
        })
        loadViewHelper?.showLoading(refresh_layout)
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

            }
        }

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as OrderBean
            when (view.id) {
                R.id.right_btn -> {
                    when (bean.status) {
                        -1, 4 -> {//已经取消  已经完成的 删除操作
                            AlertDialogFragment.newIntance()
                                .setKeyBackable(false)
                                .setCancleable(false)
                                .setCancleBtn { }
                                .setSureBtn {
                                    mViewModel.deleteOrder(bean.orderId) {
                                        if (it?.flag!!) {
                                            adapter.remove(position)
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                                .setContent("是否删除本订单")
                                .show(fragmentManager, "deleteOrder")
                        }
                        2 -> {//已经支付  未发货  提醒发货
                            mViewModel.remind(bean.orderId, bean.list[0].skuId) {
                                if (it?.flag!!) {
                                    toast("提醒发货成功")
                                }
                            }
                        }
                        3 -> {//已经发货 和已支付  点击就是完成订单
                            AlertDialogFragment.newIntance()
                                .setKeyBackable(false)
                                .setCancleable(false)
                                .setCancleBtn { }
                                .setSureBtn {
                                    mViewModel.completeOrder(bean.orderId, bean.list[0].skuId) {
                                        if (type == 2) {//待收货中的 订单完成  就直接移除列表
                                            adapter.remove(position)
                                            adapter.notifyDataSetChanged()
                                        } else {//全部中的  订单完成  就讲“待收货” 状态改成 “已完成状态”
                                            bean.status = 4
                                            adapter.notifyItemChanged(position, bean)
                                        }
                                    }
                                }
                                .setContent("请确认已经收到货物")
                                .show(fragmentManager, "comfirm_get_product")

                        }

                        0, 1 -> {//未支付和支付中的 订单状态   直接去支付
                            PayDialog.newIntance().invoke(object : PayDialog.ClickListener {
                                override fun click(v: View?, type: Int) {
                                    mViewModel.rePay(bean.orderId,type){
                                        val bundle = Bundle()
                                        val productBean = ProductBean()
                                        productBean.setSkuView(bean.list[0])
                                        productBean.setOrderId(bean.orderId)
                                        bundle.putSerializable(CreateOrderActivity.KEY_PRODUCT_BEAN,productBean)
                                        startRout(RoutUrl.Order.paySuccess, CreateOrderActivity.KEY_BUNDLE,bundle)
                                    }
                                }
                            }).show(fragmentManager, "paydiaolog")
                        }

                        5, 6 -> {//只显示左边按钮  直接跳转  退款退货详情  看进度
                            val bundle = Bundle()
                            bundle.putString(RefundDetailActivity.KEY_ORDER_ID,bean.orderId)
                            bundle.putString(RefundDetailActivity.KEY_SKU_ID,bean.list[0].skuId)
                            startRout(RoutUrl.Mine.refund_detail, RefundDetailActivity.KEY_BUNDLE,bundle)
                        }
                    }

                }

                R.id.left_btn -> {
                    val orderBean = adapter.data[position] as OrderBean
                    when (bean.status) {
                        -3, -2 -> {//只显示左边按钮  直接跳转  退款退货详情
                            val bundle = Bundle()
                            bundle.putString(RefundDetailActivity.KEY_ORDER_ID,orderBean.orderId)
                            bundle.putString(RefundDetailActivity.KEY_SKU_ID,orderBean.list[0].skuId)
                            startRout(RoutUrl.Mine.refund_detail, RefundDetailActivity.KEY_BUNDLE,bundle)
                        }
                        0, 1 -> {//未支付和支付中的 订单状态 左边按钮 换成取消订单
                            AlertDialogFragment.newIntance()
                                .setKeyBackable(false)
                                .setCancleable(false)
                                .setCancleBtn { }
                                .setSureBtn {
                                    mViewModel.cancelOrder(bean.orderId) {
                                        adapter.remove(position)
                                        adapter.notifyDataSetChanged()
                                    }

                                }
                                .setContent("是否取消定单")
                                .show(fragmentManager, "cancel_order")
                        }
                        3 -> {//已发货  查看物流
                            val bundle = Bundle()
                            bundle.putSerializable(KEY_OREDR_BEAN, orderBean)
                            startRout(RoutUrl.Mine.logistics_info, KEY_BUNDLE, bundle)
                        }
                        4 -> {//已经完成  评价
                            val bundle = Bundle()
                            bundle.putSerializable(KEY_OREDR_BEAN, orderBean)
                            startRout(RoutUrl.Comment.addComment, KEY_BUNDLE, bundle)
                        }
                    }
                }
            }
        }

        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as OrderBean
            val bundle = Bundle()
            bundle.putString(KEY_ORDER_ID, bean.orderId)
            bundle.putString(KEY_SKU_ID, bean.list[0].skuId)
            bundle.putString(KEY_PRODUCT_ID, bean.list[0].productId)
            startRout(RoutUrl.Mine.order_detail, KEY_BUNDLE, bundle)
        }
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addView(inflater, R.layout.order_fragment, container)
        return mRootView
    }

    override fun initImmersionBar() {
    }
}