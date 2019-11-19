package com.wbg.xigui.ui.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.StoreJoinViewModel
import kotlinx.android.synthetic.main.activity_store_join.*
import startRout
import toast
import java.util.*

/**
 * @author tyk
 * @date :2019/9/11 10:54
 * @des :  商家入驻
 */

@Route(path = RoutUrl.Mine.store_join, extras = RoutUrl.Extra.login)
class StoreJoinActivity : BaseXActivity<StoreJoinViewModel>(), View.OnClickListener {

    var position = 2  //商家入驻进度

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("商家入驻")
        setTitleBackground(resources.getColor(R.color.colorPrimary))
        setTitleTextColor(resources.getColor(R.color.white))
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
        addView(R.layout.activity_store_join)
    }

    override fun initData() {
        setListener()
        position = Random().nextInt()%4
        when (position) {
            0 -> {//注册入驻
                tv_one.isSelected = false
                tv_one.setTextColor(resources.getColor(R.color.fontcColorgray))
                btn_now.setBackgroundResource(R.drawable.theme_btn_bg)
                btn_now.text = "立即认证"
                btn_now.setTextColor(resources.getColor(R.color.white))
            }
            1 -> {//认证中
                tv_one.isSelected = false
                tv_one.setTextColor(resources.getColor(R.color.fontcColorgray))
                btn_now.setBackgroundResource(R.drawable.corner_23dp_stock_red)
                btn_now.text = "查询进度"
                btn_now.setTextColor(resources.getColor(R.color.theme))
            }
            2 -> {//已认证
                tv_one.isSelected = true
                tv_one.setTextColor(resources.getColor(R.color.theme))
                btn_now.setBackgroundResource(R.drawable.corner_23dp_orenge)
                btn_now.text = "发布商品"
                btn_now.setTextColor(resources.getColor(R.color.white))
            }
            3 -> {//认证失败
                tv_one.isSelected = false
                tv_one.setTextColor(resources.getColor(R.color.fontcColorgray))
                btn_now.setBackgroundResource(R.drawable.theme_btn_bg)
                btn_now.text = "重新认证"
                btn_now.setTextColor(resources.getColor(R.color.white))
            }
        }

    }

    fun setListener() {
        btn_now.setOnClickListener(this)
        ll_store_join.setOnClickListener(this)
        ll_product_about.setOnClickListener(this)
        ll_tg.setOnClickListener(this)
        ll_product_fh.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_now -> {//立即认证
                startRout(RoutUrl.Mine.commit_data)
            }
            R.id.ll_store_join -> {//商家入驻
                toast("商家入驻")
            }
            R.id.ll_product_about -> {//商品相关
                toast("商品相关")
            }
            R.id.ll_tg -> {//邀请推广
                toast("邀请推广")
            }
            R.id.ll_product_fh -> {//商品发货
                toast("商品发货")
            }
        }
    }

}