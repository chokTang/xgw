package com.wbg.xigui.ui.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hotokay.jakeefactory.ui.base.GsonUtil
import com.kw.rxbus.RxBus
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.OrderProductAdapter
import com.wbg.xigui.adapter.OrderProductAdapter2
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.*
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.viewmodel.CreateOrderViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_create_order.*
import startRout

/**
 * @author tyk
 * @date :2019/8/15 16:14
 * @des : 生成订单 （填写订单）
 */
@Route(path = RoutUrl.Order.createOrder)
class CreateOrderActivity : BaseXActivity<CreateOrderViewModel>(), View.OnClickListener {

    companion object {
        const val KEY_PRODUCT_BEAN = "key_product_bean"
        const val KEY_LIST_PRODUCT = "key_list_product_bean"
        const val KEY_KEY_MONEY = "key_money"
        const val KEY_BUNDLE = "key_bundle"
    }

    private lateinit var disposable: Disposable

    var orderProductAdapter: OrderProductAdapter? = null
    var adapter: OrderProductAdapter2? = null
    var isNowBuy = true
    var isAlipay = false
    var money = 0f
    var zqmoney = 0f
    var list: MutableList<ProductDetailBean> = arrayListOf()
    var addressId = ""

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setTitle("填写订单")
        addView(R.layout.activity_create_order)
        setListener()

        //判断是否是 立即购买 然后显示
        isNowBuy = intent.getBundleExtra(KEY_BUNDLE).get(KEY_PRODUCT_BEAN) != null

        mViewModel.context = this
        //获取默认地址
        mViewModel.getAddressList {
            if (it.list.isNullOrEmpty()) {//没有地址
                ll_address_msg.visibility = View.GONE
                tv_hint.visibility = View.VISIBLE
            } else {//有地址
                ll_address_msg.visibility = View.VISIBLE
                tv_hint.visibility = View.GONE
                tv_name_phone.text = it.list[0]!!.name + it.list[0]!!.phoneNumber
                tv_address.text =
                        it.list[0]!!.province + it.list[0]!!.city + it.list[0]!!.region + it.list[0]!!.detailAddress
                addressId = it.list[0]!!.id!!
            }
        }

    }

    fun setListener() {
        ll_address.setOnClickListener(this)
        rl_wx.setOnClickListener(this)
        tv_now_pay.setOnClickListener(this)
        rl_alipay.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        disposable = RxBus.getInstance().toObservable(X::class.java).subscribe {
            ll_address_msg.visibility = View.VISIBLE
            tv_hint.visibility = View.GONE
            tv_name_phone.text = it.name + it.phoneNumber
            tv_address.text = it.province + it.city + it.region + it.detailAddress
            addressId = it.id!!
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        //默认选中微信支付
        img_wx_select.isSelected = true
        img_alipay_select.isSelected = false

        val linearLayoutManager = LinearLayoutManager(this)
        rv_order.layoutManager = linearLayoutManager

        if (isNowBuy) {//立即购买 仅仅一种商品
            orderProductAdapter = OrderProductAdapter()
            rv_order.adapter = orderProductAdapter
            val bean = intent.getBundleExtra(KEY_BUNDLE).get(KEY_PRODUCT_BEAN) as ProductDetailBean
            list.add(bean)
            orderProductAdapter?.list = list
            orderProductAdapter?.setNewData(list)
            tv_money.text = "￥" + String.format("%.2f", list[0].orderPrice)
            // 这里的bond ：兑换比例：41%  我们要提出  41
            val persont = list[0].bond?.removePrefix("兑换比例：")!!.removeSuffix("%").toFloat() / 100
            tv_zq.text = "￥" + String.format("%.2f", list[0].orderPrice * persont)
            tv_coupon.text = "￥" + String.format("%.2f", list[0].orderPrice * persont)
            tv_pay_money.text = "实付款：￥" + String.format("%.2f", list[0].orderPrice)
        } else {  // 购物车
            val string = intent.getBundleExtra(KEY_BUNDLE).getString(KEY_LIST_PRODUCT)
            val shopcarList = GsonUtil.toList(string, ShopCartBean::class.java)
            adapter = OrderProductAdapter2()
            rv_order.adapter = adapter

            val carList: MutableList<ShopCartBean> = arrayListOf()
            for (i in 0 until shopcarList.size) {
                if (shopcarList[i].isSelected){
                    val productList: MutableList<CartGoodsBean> = arrayListOf()
                    for (j in 0 until shopcarList[i].goodsList!!.size) {
                        if (shopcarList[i].goodsList!![j].isSelected){
                            productList.add(shopcarList[i].goodsList!![j])
                        }
                    }
                    val bean = ShopCartBean()
                    bean.goodsList = productList
                    bean.merchantName = shopcarList[i].merchantName
                    carList.add(shopcarList[i])
                }

            }
            adapter?.setNewData(carList)


            var allMoney = 0f
            var zq = 0f
            for (i in 0 until adapter?.data!!.size) {
                for (j in 0 until adapter?.data!![i].goodsList!!.size) {
                    if (adapter?.data!![i].goodsList!![j].isSelected) {
                        allMoney += adapter?.data!![i].goodsList!![j].total!! * adapter?.data!![i].goodsList!![j].price!!
                        zq += adapter?.data!![i].goodsList!![j].bond!! * adapter?.data!![i].goodsList!![j].price!!
                    }
                }
            }

            money = allMoney
            zqmoney = zq
            tv_money.text = "￥${StrUtil.subZeroAndDot(String.format("%.2f", allMoney))}"
            tv_zq.text = "￥${StrUtil.subZeroAndDot(String.format("%.2f", zq))}"
            tv_coupon.text = "￥${StrUtil.subZeroAndDot(String.format("%.2f", zq))}"
            tv_pay_money.text = "￥${StrUtil.subZeroAndDot(String.format("%.2f", allMoney))}"

        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_address -> {
                startRout(RoutUrl.Mine.addressList)
            }
            R.id.rl_wx -> {//微信
                img_wx_select.isSelected = true
                img_alipay_select.isSelected = false
                isAlipay = false
            }
            R.id.rl_alipay -> {//支付宝
                img_wx_select.isSelected = false
                img_alipay_select.isSelected = true
                isAlipay = true
            }
            R.id.tv_now_pay -> {//生成订单立即支付
                val mlist: MutableList<CreateOrderPamas.CreateSkuOrder> = arrayListOf()
                val pamas = CreateOrderPamas()
                pamas.addressId = addressId
                if (isNowBuy) {
                    pamas.isFromShoppingBag = 0
                    val createSkuOrder = CreateOrderPamas.CreateSkuOrder()
                    createSkuOrder.merchantId = list[0].merchantId
                    createSkuOrder.productId = list[0].productId
                    createSkuOrder.skuId = list[0].skuId
                    createSkuOrder.total = list[0].num!!
                    mlist.add(createSkuOrder)
                    pamas.list = mlist
                } else {
                    pamas.isFromShoppingBag = 1
                    for (i in 0 until adapter?.data!!.size) {
                        for (j in 0 until adapter?.data!![i].goodsList!!.size) {
                            val createSkuOrder = CreateOrderPamas.CreateSkuOrder()
                            createSkuOrder.merchantId = adapter?.data!![i].goodsList!![j].merchantId
                            createSkuOrder.productId = adapter?.data!![i].goodsList!![j].productId
                            createSkuOrder.skuId = adapter?.data!![i].goodsList!![j].skuId
                            createSkuOrder.total = adapter?.data!![i].goodsList!![j].total!!
                            mlist.add(createSkuOrder)
                        }
                    }
                    pamas.list = mlist
                }
                pamas.memberId = XApplication.instance.getMemberId()
                if (isAlipay) {
                    pamas.payment = 1
                } else {
                    pamas.payment = 2
                }

                mViewModel.createProductOrder(pamas) {

                    if (it) {//成功跳转  成功页面
                        if (isNowBuy) {//立即购买
                            val bundle = Bundle()
                            val productBean = ProductBean()
                            productBean.type = 0
                            productBean.setProductDetailBean(list[0])
                            productBean.setOrderId(mViewModel.orderId)
                            bundle.putSerializable(KEY_PRODUCT_BEAN, productBean)
                            startRout(RoutUrl.Order.paySuccess, KEY_BUNDLE, bundle)
                        } else {//购物车
                            val bundle = Bundle()
                            val productBean = ProductBean()
                            productBean.type = 1
                            productBean.money = money
                            productBean.zq = zqmoney
                            productBean.setOrderId(mViewModel.orderId)
                            bundle.putSerializable(KEY_PRODUCT_BEAN, productBean)
                            startRout(RoutUrl.Order.paySuccess, KEY_BUNDLE, bundle)
                        }
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister(disposable)
    }
}