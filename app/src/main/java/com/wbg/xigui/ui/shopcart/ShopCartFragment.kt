package com.wbg.xigui.ui.shopcart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.hotokay.jakeefactory.ui.base.GsonUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.CartProductAdapter
import com.wbg.xigui.adapter.ProductRecommendAdapter
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.ProductRecBean
import com.wbg.xigui.bean.ShopCartBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.order.CreateOrderActivity.Companion.KEY_BUNDLE
import com.wbg.xigui.ui.order.CreateOrderActivity.Companion.KEY_KEY_MONEY
import com.wbg.xigui.ui.order.CreateOrderActivity.Companion.KEY_LIST_PRODUCT
import com.wbg.xigui.ui.product.ProductDetailActivity
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.viewmodel.ShopCartViewModel
import com.wbg.xigui.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.shop_cart_fragment.*
import startRout

/**
 * @author xyx
 * @date :2019/6/17 15:26
 */
class ShopCartFragment : BaseXFragment<ShopCartViewModel>(), View.OnClickListener {

    var adapter: CartProductAdapter? = null
    var recommendAdapter: ProductRecommendAdapter? = null
    var productId: String? = null
    var skuid: String? = null
    var isAllSelected = false

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        setListener()
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_product.layoutManager = linearLayoutManager
        adapter = CartProductAdapter()
        rv_product.adapter = adapter
        mViewModel.getCarProductList {
            adapter?.setNewData(it)
        }
        mViewModel.getProductRecList {
            showRecommendProduct(it)
        }

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.data[position] as ShopCartBean
            when (view.id) {
                R.id.check_img_first -> {
                    item.isSelected = !item.isSelected
                    if (item.isSelected) {//title选了 下面的 子项全选
                        for (i in 0 until item.goodsList!!.size) {
                            item.goodsList!![i].isSelected = true
                        }
                    } else {//title选了 下面的 子项全选
                        for (i in 0 until item.goodsList!!.size) {
                            item.goodsList!![i].isSelected = false
                        }
                    }
                    adapter.notifyItemChanged(position)
                    showPriceAndNum()
                }
            }
        }

        adapter?.invoke(object : CartProductAdapter.ClickListener {
            override fun refresh() {
                showPriceAndNum()
            }

            override fun ItemClick(v: View?, firstPosition: Int, position: Int) {
                val bean = adapter?.data!![firstPosition] as ShopCartBean
                val item = bean.goodsList!![position]
                val bundle = Bundle()
                bundle.putString(ProductDetailActivity.KEY_PRODUCYID, item.productId)
                bundle.putString(ProductDetailActivity.KEY_SKU_ID, item.skuId)
                bundle.putInt(ProductDetailActivity.KEY_POSITION, 0)
                startRout(RoutUrl.Product.home, ProductDetailActivity.KEY_BUNDLE, bundle)
            }
        })
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addView(inflater, R.layout.shop_cart_fragment, container)
        return mRootView
    }


    fun setListener() {
        tv_go_pay.setOnClickListener(this)
        tv_all.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_go_pay -> {
                val list = adapter?.data as MutableList<ShopCartBean>
                val bundle = Bundle()
                bundle.putString(KEY_LIST_PRODUCT, GsonUtil.toJson(list))
                bundle.putString(KEY_KEY_MONEY,tv_all_money.text.toString())
                startRout(RoutUrl.Order.createOrder, KEY_BUNDLE, bundle)
            }
            R.id.tv_all -> {
                var allNum = 0
                var allMoney = 0f
                if (isAllSelected) {
                    for (i in 0 until adapter?.data!!.size) {
                        adapter?.data!![i].isSelected = false
                    }
                    allNum = 0
                    allMoney = 0f
                    isAllSelected = !isAllSelected
                } else {
                    for (i in 0 until adapter?.data!!.size) {
                        adapter?.data!![i].isSelected = true
                        allNum += adapter?.data!![i].goodsList!!.size
                        for (j in 0 until adapter?.data!![i].goodsList!!.size) {
                            allMoney += adapter?.data!![i].goodsList!![j].total!! * adapter?.data!![i].goodsList!![j].price!!
                        }
                    }
                    isAllSelected = !isAllSelected
                }
                adapter?.notifyDataSetChanged()
                tv_all.isSelected = isAllSelected
                tv_all_money.text = "￥${StrUtil.subZeroAndDot(String.format("%.2f", allMoney))}"
                tv_go_pay.text = "去结算（${allNum}）"
            }
        }
    }

    /**
     * 计算并显示 总的物品和总的价钱
     */
    @SuppressLint("SetTextI18n")
    fun showPriceAndNum() {
        var allNum = 0
        var allMoney = 0f
        for (i in 0 until adapter?.data!!.size) {
            if (adapter?.data!![i].isSelected) {
                allNum += adapter?.data!![i].goodsList!!.size
            }
            for (j in 0 until adapter?.data!![i].goodsList!!.size) {
                if (adapter?.data!![i].goodsList!![j].isSelected) {
                    allMoney += adapter?.data!![i].goodsList!![j].total!! * adapter?.data!![i].goodsList!![j].price!!
                }
            }
        }
        tv_all_money.text = "￥${StrUtil.subZeroAndDot(String.format("%.2f", allMoney))}"
        tv_go_pay.text = "去结算（${allNum}）"
    }


    /**
     * 显示推荐商品模块
     */

    fun showRecommendProduct(list: List<ProductRecBean>?) {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        rv_recommend_product.layoutManager = gridLayoutManager
        recommendAdapter = ProductRecommendAdapter()
        rv_recommend_product.adapter = recommendAdapter
        rv_recommend_product.isNestedScrollingEnabled = false

        rv_recommend_product.addItemDecoration(DividerGridItemDecoration(activity, R.drawable.listdivider_white_10))

        recommendAdapter?.setNewData(list)

        recommendAdapter?.setOnItemClickListener { adapter, view, position ->
            val item = adapter.data[position] as ProductRecBean
            val bundle = Bundle()
            bundle.putString(ProductDetailActivity.KEY_PRODUCYID, item.productId)
            bundle.putString(ProductDetailActivity.KEY_SKU_ID, item.skuId)
            bundle.putInt(ProductDetailActivity.KEY_POSITION, 0)
            startRout(RoutUrl.Product.home, ProductDetailActivity.KEY_BUNDLE, bundle)
        }
        recommendAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.data[position] as ProductRecBean
            when (view.id) {
                R.id.rl_add_car -> {
                    val bundle = Bundle()
                    bundle.putString(ProductDetailActivity.KEY_PRODUCYID, item.productId)
                    bundle.putString(ProductDetailActivity.KEY_SKU_ID, item.skuId)
                    bundle.putInt(ProductDetailActivity.KEY_POSITION, 0)
                    startRout(RoutUrl.Product.home, ProductDetailActivity.KEY_BUNDLE, bundle)
                }
            }
        }
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true, 0.2f)
                .init()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            initData()
        }
    }
}