package com.wbg.xigui.ui.near

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.MerchantAdapter
import com.wbg.xigui.adapter.SearchProducAdapter
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.SearchProduct
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.near.SearchResultActivity.Companion.KEY_WORD
import com.wbg.xigui.ui.near.StoreDetailActivity.Companion.KEY_STORE_BEAN
import com.wbg.xigui.ui.product.ProductDetailActivity
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.SearchGoodsViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.search_goods_fragment.*
import log
import startRout

/**
 * @author xyx
 * @date :2019/6/21 15:27
 */
class SearchGoodsFragment : BaseXFragment<SearchGoodsViewModel>(), View.OnClickListener {
    var adapter: SearchProducAdapter? = null
    var adapterMechant: MerchantAdapter? = null
    var word = ""
    var type = 1//排序方式 1综合 2 销量 3价格升序 4价格降序 5商家兑换比例升序 6商家兑换比例降序 不填则默认是综合
    var searchType = 0 //0商品  1商店
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        word = arguments?.get(KEY_WORD) as String
        log(word)
    }

    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addView(inflater, R.layout.search_goods_fragment, container)
        return mRootView
    }

    override fun initData() {
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_result.layoutManager = linearLayoutManager
        if (searchType == 0) {//商品
            price_tv.text = "价格"
            adapter = SearchProducAdapter()
            rv_result.adapter = adapter

            loadViewHelper = (object : LoadViewHelper(context!!) {
                override fun action() {
                    mViewModel.refresh(word, type) {
                        adapter?.setNewData(it)
                    }
                }
            })

            loadViewHelper?.setLayoutId(R.layout.search_empty)
            mViewModel.getData(word, type) {
                adapter?.setNewData(it)
            }
            adapter?.setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as SearchProduct
                val bundle = Bundle()
                bundle.putString(ProductDetailActivity.KEY_PRODUCYID, item.productId)
                bundle.putString(ProductDetailActivity.KEY_SKU_ID, item.skuId)
                bundle.putInt(ProductDetailActivity.KEY_POSITION, 0)
                startRout(RoutUrl.Product.home, ProductDetailActivity.KEY_BUNDLE, bundle)
            }
        } else {//商家
            price_tv.text = "兑换比例"
            adapterMechant = MerchantAdapter()
            rv_result.adapter = adapterMechant

            loadViewHelper = (object : LoadViewHelper(context!!) {
                override fun action() {
                    mViewModel.refreshMechant(word, type) {
                        adapterMechant?.setNewData(it)
                    }
                }
            })

            loadViewHelper?.setLayoutId(R.layout.search_empty)
            mViewModel.getDataMerchant(word, type) {
                adapterMechant?.setNewData(it)
            }

            adapterMechant?.setOnItemClickListener { adapter, view, position ->
                 val item = adapter.data[position] as StoreBean
                startRout(RoutUrl.Near.store_detail, KEY_STORE_BEAN, item)
            }
        }

        setListener()
    }


    fun setListener() {
        refresh_layout.setOnRefreshListener {
            if (searchType == 0) {
                mViewModel.refresh(word, type) {
                    adapter?.setNewData(it)
                }
            } else {
                mViewModel.refreshMechant(word, type) {
                    adapterMechant?.setNewData(it)
                }
            }

        }
        refresh_layout.setOnLoadMoreListener {
            if (searchType == 0) {
                mViewModel.getData(word, type) {
                    adapter?.setNewData(it)
                }
            } else {
                mViewModel.getDataMerchant(word, type) {
                    adapterMechant?.setNewData(it)
                }
            }

        }
        all_rl.setOnClickListener(this)
        sales_rl.setOnClickListener(this)
        price_rl.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        setNormalTv()
        when (v?.id) {
            R.id.all_rl -> {
                all_tv.isSelected = true
                type = 1
                val dra = ResourcesUtil.getDrawable(R.drawable.icon_sort_normal)
                dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
                price_tv.setCompoundDrawables(null, null, dra, null)
            }
            R.id.sales_rl -> {
                sales_tv.isSelected = true
                type = 2
            }
            R.id.price_rl -> {
                price_tv.isSelected = true
                if (searchType == 0) {
                    val dra = if (type == 4) {
                        type = 3
                        ResourcesUtil.getDrawable(R.drawable.icon_sort_up)
                    } else {
                        type = 4
                        ResourcesUtil.getDrawable(R.drawable.icon_sort_down)
                    }
                    dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
                    price_tv.setCompoundDrawables(null, null, dra, null)
                } else {
                    val dra = if (type == 6) {
                        type = 5
                        ResourcesUtil.getDrawable(R.drawable.icon_sort_up)
                    } else {
                        type = 6
                        ResourcesUtil.getDrawable(R.drawable.icon_sort_down)
                    }
                    dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
                    price_tv.setCompoundDrawables(null, null, dra, null)
                }


            }
        }
        refresh()


    }

    override fun initImmersionBar() {

    }

    fun setNormalTv() {
        all_tv.isSelected = false
        sales_tv.isSelected = false
        price_tv.isSelected = false
    }

    fun refresh() {
        if (searchType == 0) {
            mViewModel.refresh(word, type) {
                adapter?.setNewData(it)
            }
        } else {
            mViewModel.refreshMechant(word, type) {
                adapterMechant?.setNewData(it)
            }
        }

    }
}