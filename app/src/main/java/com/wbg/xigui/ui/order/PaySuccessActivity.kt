package com.wbg.xigui.ui.order

import android.annotation.SuppressLint
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.ProductRecommendAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.ProductBean
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.PaySuccessViewModel
import com.wbg.xigui.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.activity_product_pay_success.*
import kotlinx.android.synthetic.main.item_layout_recommend_product.*

/**
 * @author tyk
 * @date :2019/8/21 8:55
 * @des : 商品订单支付成功页面
 */

@Route(path = RoutUrl.Order.paySuccess)
class PaySuccessActivity : BaseXActivity<PaySuccessViewModel>() {


    var productId = ""
    var orderId = ""
    var bean: ProductBean? = null
    var recommendAdapter: ProductRecommendAdapter? = null


    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("支付成功")
        addView(R.layout.activity_product_pay_success)
        val bundle = intent.getBundleExtra(CreateOrderActivity.KEY_BUNDLE)
        bean = bundle?.get(CreateOrderActivity.KEY_PRODUCT_BEAN) as ProductBean
        orderId = bean?.orderId!!
        productId = if (bean?.getProductDetailBean() != null) {
            bean?.getProductDetailBean()!!.productId.toString()
        } else {
            bean?.getSkuView()!!.productId!!
        }

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        if (bean?.type == 0){//立即购物
            var orderPrice = 0f
            var bond = ""
            if (bean?.getProductDetailBean() != null) {
                orderPrice = bean?.getProductDetailBean()!!.orderPrice
                bond = bean?.getProductDetailBean()!!.bond!!
                tv_money.text = "￥" + String.format("%.2f", orderPrice)
                val persont = bond.removePrefix("兑换比例：").removeSuffix("%").toFloat() / 100
                tv_pay_change_money.text = "￥" + String.format("%.2f", orderPrice * persont)
            } else {
                orderPrice = bean?.getSkuView()!!.price
                tv_money.text = "￥" + String.format("%.2f", orderPrice)
                tv_pay_change_money.text = "￥" + String.format("%.2f", orderPrice * bean?.getSkuView()!!.bondPortion)
            }

        }else{//购物车
            tv_money.text = "￥" + String.format("%.2f", bean?.money)
            tv_pay_change_money.text = "￥" + String.format("%.2f", bean?.zq)
        }


        mViewModel.getOrderRecList(productId, orderId) {
            showRecommendProduct(it)
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

        rv_recommend_product.addItemDecoration(DividerGridItemDecoration(this, R.drawable.listdivider_white_10))

        recommendAdapter?.setNewData(list)
    }
}