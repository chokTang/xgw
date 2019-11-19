package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.MyOrderProductAdapter
import com.wbg.xigui.adapter.ProductRecommendAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.dialog.AlertDialogFragment
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.comment.AddCommentActivity
import com.wbg.xigui.ui.product.ProductDetailActivity
import com.wbg.xigui.utils.ZZDate
import com.wbg.xigui.viewmodel.OrderDetailViewModel
import com.wbg.xigui.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.order_detail_activity.*
import startRout

/**
 * @author tyk
 * @date :2019/7/8 10:18
 * @des  订单详情
 */
@Route(path = RoutUrl.Mine.order_detail, extras = RoutUrl.Extra.login)
class OrderDetailActivity : BaseXActivity<OrderDetailViewModel>(), View.OnClickListener {


    companion object {
        const val KEY_ORDER_ID = "key_order_id"
        const val KEY_PRODUCT_ID = "key_product_id"
    }

    var recommendAdapter: ProductRecommendAdapter? = null
    var adapter: MyOrderProductAdapter? = null
    var skuId = ""
    var orderId = ""
    var productId = ""
    var bean: OrderBean? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        addView(R.layout.order_detail_activity)
        ImmersionBar.with(this).reset()
        ImmersionBar.with(this).titleBar(top_title_fl).init()

        val bundle = intent.getBundleExtra(AddCommentActivity.KEY_BUNDLE)
        if (bundle != null) {
            skuId = bundle.getString(ProductDetailActivity.KEY_SKU_ID)
            orderId = bundle.getString(KEY_ORDER_ID)
            productId = bundle.getString(KEY_PRODUCT_ID)
        }
        setListener()
    }

    override fun initData() {
        mViewModel.getOrderDetail(orderId, skuId) {
            bean = it
            showView(it!!)
        }

        mViewModel.getProductRecList(productId, skuId) {
            showRecommendProduct(it)
        }
    }


    fun setListener() {
        fl_left.setOnClickListener(this)
        fl_right.setOnClickListener(this)
        left_btn.setOnClickListener(this)
        right_btn.setOnClickListener(this)
        back_area.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun showView(bean: OrderBean) {
        tv_name.text = bean.consigneeName
        if (!TextUtils.isEmpty(bean.consigneeMobile) && bean.consigneeMobile.length == 11) {
            val num = bean.consigneeMobile.replaceRange(2, 7, "*****")
            tv_phone.text = num
        }
        tv_address.text = bean.consigneeAddress
        val linearLayoutManager = LinearLayoutManager(this)
        rv_product.layoutManager = linearLayoutManager
        adapter = MyOrderProductAdapter()
        rv_product.adapter = adapter
        adapter?.setNewData(bean.list)
        tv_all_money.text = bean.totalAmount.toString()
        tv_order_number.text = bean.orderId
        tv_order_time.text = bean.createTime
        when (bean.payment) {
            1 -> {
                tv_pay_type.text = "支付宝"
            }
            2 -> {
                tv_pay_type.text = "微信"
            }
            3 -> {
                tv_pay_type.text = "小程序"
            }
        }
        tv_logistics_type.text = "快递方式：" + bean.list[0].comName
        tv_logistics_number.text = "运单编号：" + bean.list[0].trackeNum

        when (bean.status) {

            -3 -> {//已退货
                tv_status_fahuo.text = "买家已退货"
                tv_the_remaining_time.text = ""
                ll_logistics_msg.visibility = View.VISIBLE
                if (bean.list[0].list.isNotEmpty()) {
                    tv_logistics_status.text = "物流状态:" + bean.list[0].list[0].context
                    tv_logistics_time.text = "物流状态:" + bean.list[0].list[0].ftime
                } else {
                    tv_logistics_status.text = "未更新物流状态"
                    tv_logistics_time.text = ZZDate.getDate()
                }
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.VISIBLE
                fl_right.visibility = View.GONE
                left_btn.visibility = View.VISIBLE
                right_btn.visibility = View.GONE
                left_btn.text = "查询详情"
            }
            -2 -> {//已退款
                tv_status_fahuo.text = "已退款"
                tv_the_remaining_time.text = ""
                ll_logistics_msg.visibility = View.VISIBLE
                if (bean.list[0].list.isNotEmpty()) {
                    tv_logistics_status.text = "物流状态:" + bean.list[0].list[0].context
                    tv_logistics_time.text = "物流状态:" + bean.list[0].list[0].ftime
                } else {
                    tv_logistics_status.text = "未更新物流状态"
                    tv_logistics_time.text = ZZDate.getDate()
                }
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.VISIBLE
                fl_right.visibility = View.GONE
                left_btn.visibility = View.VISIBLE
                right_btn.visibility = View.GONE
                left_btn.text = "查询详情"
            }
            -1 -> {//已经取消
                tv_status_fahuo.text = "已取消订单"
                tv_the_remaining_time.text = ""
                ll_logistics_msg.visibility = View.GONE
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.GONE
                fl_right.visibility = View.GONE
                left_btn.visibility = View.GONE
                right_btn.visibility = View.VISIBLE
                right_btn.text = "删除"
            }
            0, 1 -> {//待支付  支付中
                tv_status_fahuo.text = "等待买家支付"
                tv_the_remaining_time.text = "逾期未付款，订单将自动取消"
                ll_logistics_msg.visibility = View.GONE
                ll_pay_type.visibility = View.VISIBLE
                tv_pay_type.visibility = View.GONE
                fl_right.visibility = View.GONE
                left_btn.visibility = View.VISIBLE
                right_btn.visibility = View.VISIBLE
                left_btn.text = "取消订单"
                right_btn.text = "立即支付"
            }
            2 -> {//已支付未发货
                tv_status_fahuo.text = "卖家未发货"
                tv_the_remaining_time.text = "请耐心等候商家会尽快为您发货"
                ll_logistics_msg.visibility = View.VISIBLE
                tv_logistics_status.text = "物流状态：商家正在积极出库中"
                tv_logistics_time.text = ZZDate.getDate()
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.VISIBLE
                fl_right.visibility = View.VISIBLE
                left_btn.visibility = View.GONE
                right_btn.visibility = View.VISIBLE
                right_btn.text = "提醒发货"
            }
            3 -> {//待收货
                tv_status_fahuo.text = "卖家已发货"
                tv_the_remaining_time.text = bean.list[0].updateTime
                ll_logistics_msg.visibility = View.VISIBLE
                if (bean.list[0].list.isNotEmpty()) {
                    tv_logistics_status.text = "物流状态:" + bean.list[0].list[0].context
                    tv_logistics_time.text = "物流状态:" + bean.list[0].list[0].ftime
                } else {
                    tv_logistics_status.text = "未更新物流状态"
                    tv_logistics_time.text = ZZDate.getDate()
                }
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.VISIBLE
                fl_right.visibility = View.VISIBLE
                left_btn.visibility = View.VISIBLE
                right_btn.visibility = View.VISIBLE
                left_btn.text = "查看物流"
                right_btn.text = "确认收货"
            }
            4 -> {//已完成
                tv_status_fahuo.text = "交易完成"
                tv_the_remaining_time.text = "感谢你在喜归网购物，欢迎再次光临！"
                ll_logistics_msg.visibility = View.VISIBLE
                tv_logistics_status.text = "物流状态：已成功签收"
                tv_logistics_time.text = ZZDate.getDate()
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.VISIBLE
                fl_right.visibility = View.GONE
                left_btn.visibility = View.VISIBLE
                right_btn.visibility = View.VISIBLE
                left_btn.text = "评价"
                right_btn.text = "删除订单"
            }
            5, 6 -> {//申请退款
                tv_status_fahuo.text = "申请退款退货中"
                tv_the_remaining_time.text = "感谢你在喜归网购物，欢迎再次光临！"
                ll_logistics_msg.visibility = View.VISIBLE
                tv_logistics_status.text = "物流状态：已成功签收"
                tv_logistics_time.text = ZZDate.getDate()
                ll_pay_type.visibility = View.GONE
                tv_pay_type.visibility = View.VISIBLE
                fl_right.visibility = View.GONE
                left_btn.visibility = View.GONE
                right_btn.visibility = View.VISIBLE
                right_btn.text = "查询进度"
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_area -> {
                finish()
            }
            R.id.fl_left -> {//联系卖家按钮

            }
            R.id.fl_right -> {//申请退款按钮
                val bundle = Bundle()
                bundle.putSerializable(AddCommentActivity.KEY_OREDR_BEAN, bean)
                startRout(RoutUrl.Mine.refund, AddCommentActivity.KEY_BUNDLE, bundle)
            }
            R.id.left_btn -> {//最下面左边按钮
                when (bean?.status) {
                    3 -> {
                        val bundle = Bundle()
                        bundle.putSerializable(AddCommentActivity.KEY_OREDR_BEAN, bean)
                        startRout(RoutUrl.Mine.logistics_info, AddCommentActivity.KEY_BUNDLE, bundle)
                    }
                }
            }
            R.id.right_btn -> {//最下面右边按钮
                when (bean?.status) {
                    3 -> {
                        AlertDialogFragment.newIntance()
                                .setKeyBackable(false)
                                .setCancleable(false)
                                .setCancleBtn { }
                                .setSureBtn {
                                    mViewModel.completeOrder(bean?.orderId, bean!!.list[0].skuId) {
                                        if (it?.flag!!) {
                                            finish()
                                        }
                                    }
                                }
                                .setContent("请确认已经收到货物")
                                .show(supportFragmentManager, "comfirm_get_product")
                    }
                }
            }
        }
    }

    /**
     * 显示推荐商品模块
     */

    fun showRecommendProduct(list: List<ProductRecBean>?) {
        val gridLayoutManager = GridLayoutManager(this, 2)
        rv_recommend_product.layoutManager = gridLayoutManager
        recommendAdapter = ProductRecommendAdapter()
        rv_recommend_product.adapter = recommendAdapter

        rv_recommend_product.addItemDecoration(
                DividerGridItemDecoration(
                        this,
                        R.drawable.listdivider_white_10
                )
        )

        recommendAdapter?.setNewData(list)
    }
}