package com.wbg.xigui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyf.immersionbar.ImmersionBar
import com.kw.rxbus.RxBus
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bus.RxEvent
import com.wbg.xigui.databinding.TestFragmentBinding
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.PicSelectorUtil
import com.wbg.xigui.viewmodel.TestFragmentModel
import kotlinx.android.synthetic.main.test_fragment.*

class TestFragment : BaseXFragment<TestFragmentModel>() {
    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
    }

    var type = 0
    var binding: TestFragmentBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.get("type") as Int
    }

    override fun initData() {
        ps.text = "hello fragment$type"
        ps.setOnClickListener {
            PicSelectorUtil.pick(this)
        }
        binding?.run {
            model = mViewModel
            mViewModel.url.set("https://s3m.milkjpg.com/galileo/654114-c6458d6af5019c50e3130c4296894cb1.jpg")
            img.setOnClickListener {
                RxBus.getInstance().send(RxEvent("测试"))
            }
        }

    }

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(layoutInflater, R.layout.test_fragment, container)
        return mRootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!= Activity.RESULT_OK){
            return
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            if (selectList.isNotEmpty()) {
                GlideUtil.loadImg(selectList[0].path, img, context!!)
            }
        }
    }
}