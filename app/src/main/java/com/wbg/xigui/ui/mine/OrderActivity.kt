package com.wbg.xigui.ui.mine

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.tab.XPagerIndicator
import com.wbg.xigui.utils.tab.XPagerTitleView
import kotlinx.android.synthetic.main.order_activity.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import java.util.*

/**
 * @author xyx
 * @date :2019/7/5 16:31
 */
@Route(path = RoutUrl.Mine.order, extras = RoutUrl.Extra.login)
class OrderActivity : BaseXActivity<BaseViewModel>() {
    companion object{
        const val KEY_TYPE_POSITION = "type_position"
    }
    val mDataList = arrayOf("全 部", "待付款", "待收货", "已完成")
    var type = 0//0全部1待付款2待收货3已完成
    private val fragmentList = ArrayList<Fragment>()
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("我的订单")
        addView(R.layout.order_activity)
        type = intent.getIntExtra(KEY_TYPE_POSITION, 0)
    }

    override fun initData() {
        var commonNavigator = CommonNavigator(this)
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

        fragmentList.addAll(arrayListOf(OrderFragment(), OrderFragment(), OrderFragment(), OrderFragment()))

        view_pager.offscreenPageLimit = 4
        view_pager.adapter = ViewpagerAdapter(supportFragmentManager)
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
        view_pager.currentItem = type
    }

    internal inner class ViewpagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt("type", i)
            fragmentList[i].arguments = bundle
            return fragmentList[i]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }
}