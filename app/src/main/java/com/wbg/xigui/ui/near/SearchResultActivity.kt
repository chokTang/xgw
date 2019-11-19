package com.wbg.xigui.ui.near

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import kotlinx.android.synthetic.main.search_result_activity.*

/**
 * @author xyx
 * @date :2019/6/21 16:03
 */
@Route(path = RoutUrl.Near.search_result)
class SearchResultActivity : BaseXActivity<BaseViewModel>() {

    companion object{
        const val KEY_WORD = "key_word"
    }

    private var currentFragment: Fragment? = null
    var word = ""
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        addView(R.layout.search_result_activity)
    }

    override fun initData() {
        word = intent.getStringExtra(KEY_WORD)
        replaceFragment(0)

        goods_tv.isSelected = true
        store_tv.isSelected = false

        goods_tv.setOnClickListener {
            goods_tv.isSelected = true
            store_tv.isSelected = false
            replaceFragment(0)
        }
        store_tv.setOnClickListener {
            goods_tv.isSelected = false
            store_tv.isSelected = true
            replaceFragment(1)
        }
        back.setOnClickListener { onBackPressed() }
    }

    private fun replaceFragment(type: Int) {
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag("flag$type")
        val bundle = Bundle()
        bundle.putString(KEY_WORD, word)
        if (currentFragment == null) {
            when (type) {
                0 -> {//商品
                    currentFragment = SearchGoodsFragment()
                    (currentFragment as SearchGoodsFragment).searchType = 0
                }
                1 -> {//店铺
                    currentFragment = SearchGoodsFragment()
                    (currentFragment as SearchGoodsFragment).searchType =1
                }
            }
            currentFragment!!.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.content, currentFragment!!, "flag$type").commit()
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()//先hide一次  触发懒加载逻辑
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }
}