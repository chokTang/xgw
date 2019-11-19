package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.WebViewActivity.Companion.KEY_LOAD_URL
import com.wbg.xigui.utils.AppUtil
import com.wbg.xigui.viewmodel.AboutUsViewModel
import com.wbg.xlib.net.RetrofitClient
import kotlinx.android.synthetic.main.activity_about_us.*
import startRout

/**
 * @author tyk
 * @date :2019/9/5 17:27
 * @des :
 */
@Route(path = RoutUrl.Mine.about_us)
class AboutUsActivity : BaseXActivity<AboutUsViewModel>(),View.OnClickListener {


    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        addView(R.layout.activity_about_us)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        tv_version_code.text = "V${AppUtil.getVersionName()}"
        tv_privice.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_privice->{
                startRout(RoutUrl.Common.web_view,KEY_LOAD_URL,RetrofitClient.h5+"page/privacy.html")
            }
        }
    }
}