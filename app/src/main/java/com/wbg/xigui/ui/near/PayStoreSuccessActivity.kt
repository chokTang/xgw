package com.wbg.xigui.ui.near

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.PaySuccessBean
import com.wbg.xigui.databinding.PaySuccessActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.PayStoreSuccessViewModel
import com.wbg.xigui.widget.AndroidBug5497Workaround
import kotlinx.android.synthetic.main.pay_success_activity.*
import log

/**
 * @author xyx
 * @date :2019/6/20 16:33
 */
@Route(path = RoutUrl.Near.pay_store_success)
class PayStoreSuccessActivity : BaseXActivity<PayStoreSuccessViewModel>() {
    var binding: PaySuccessActivityBinding? = null
    var bean: PaySuccessBean? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("支付成功")
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding = bindLayout(R.layout.pay_success_activity)
    }

    override fun initData() {
        bean = intent.getSerializableExtra("bean") as PaySuccessBean
        bean?.run {
            pay_num_tv.text = payNum
            pay_way_tv.text = way
            real_pay_num_tv.text = payNum
            exchange_num_tv.text = exchangeNum
        }
        binding?.run {
            model = mViewModel
            mViewModel.getData()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log("====oncreate===")
        super.onCreate(savedInstanceState)
        AndroidBug5497Workaround.assistActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("====ondestory===")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!= Activity.RESULT_OK){
            return
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            if (selectList.isNotEmpty()) {
                mViewModel.refreshPic(selectList)
            }
        }
    }
}