package com.wbg.xigui.ui.product

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.ViewPagerAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.WebViewFragment
import com.wbg.xigui.utils.tab.XPagerIndicator
import com.wbg.xigui.utils.tab.XPagerTitleView
import com.wbg.xigui.viewmodel.ProductDetailViewModel
import kotlinx.android.synthetic.main.activity_product_home.*
import kotlinx.android.synthetic.main.order_activity.mag_indicator
import kotlinx.android.synthetic.main.order_activity.view_pager
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import java.util.*

/**
 * @author tyk
 * @date :2019/8/12 10:46
 * @des : 商品详情页
 */

@Route(path = RoutUrl.Product.home)
class ProductDetailActivity : BaseXActivity<ProductDetailViewModel>(), View.OnClickListener {


    companion object {
        const val url = "http://47.96.104.95:8087/product/more?productId="
        const val KEY_POSITION = "key_position"  //默认显示  第几个碎片
        const val KEY_PRODUCYID = "key_productID"
        const val KEY_SKU_ID = "key_skuId"
        const val KEY_BUNDLE = "key_bundle"
    }

    val mDataList = arrayOf("商品", "详情", "评论")
    var position = 0//0商品1详情2评论
    var productId: String? = ""
    var skuId: String? = ""
    private val fragmentList = ArrayList<Fragment>()
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        addView(R.layout.activity_product_home)
        val bundle = intent.getBundleExtra(KEY_BUNDLE)
        position = bundle.getInt(KEY_POSITION)
        productId = bundle.getString(KEY_PRODUCYID)
        skuId = bundle.getString(KEY_SKU_ID)

        rl_back.setOnClickListener(this)
    }

    override fun initData() {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = (object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitleView = XPagerTitleView(context)
                clipPagerTitleView.text = mDataList[index]
                clipPagerTitleView.setOnClickListener { view_pager.currentItem = index }
                return clipPagerTitleView
            }

            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return XPagerIndicator(context)
            }

        })
        mag_indicator.navigator = commonNavigator

        val productDetailHomeFragment = ProductDetailHomeFragment()
        val commentListFragment = CommentListFragment()
        val webViewFragment = WebViewFragment()
        productDetailHomeFragment.productId = productId
        productDetailHomeFragment.skuid = skuId
        commentListFragment.id = productId
        webViewFragment.loadUrl = url + productId
        fragmentList.addAll(arrayListOf(productDetailHomeFragment, webViewFragment, commentListFragment))

        view_pager.offscreenPageLimit = 3
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        view_pager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                commonNavigator.onPageScrollStateChanged(p0)
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                commonNavigator.onPageScrolled(p0, p1, p2)
            }

            override fun onPageSelected(p0: Int) {
                commonNavigator.onPageSelected(p0)
            }

        })
        view_pager.currentItem = position
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_back -> {
                onBackPressed()
            }
        }
    }
}