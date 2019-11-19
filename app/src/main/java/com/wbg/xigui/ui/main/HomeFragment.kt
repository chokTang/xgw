package com.wbg.xigui.ui.main

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gyf.immersionbar.ImmersionBar
import com.hotokay.jakeefactory.ui.base.GsonUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.ScanCodeBean
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.databinding.HomeFragmentBinding
import com.wbg.xigui.push.ExampleUtil
import com.wbg.xigui.push.MyReceiver
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.WebViewActivity
import com.wbg.xigui.utils.AesEncryptUtils
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import log
import pub.devrel.easypermissions.EasyPermissions
import startRout
import toast


class HomeFragment : BaseXFragment<HomeFragmentViewModel>() {
    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
    }
    var mMessageReceiver: MessageReceiver? = null

    var binding: HomeFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        registerMessageReceiver()

        binding?.run {
            var bannerParams = card_view.layoutParams as FrameLayout.LayoutParams
            bannerParams.height = ((ResourcesUtil.getScreenWidth() - ResourcesUtil.dip2px(30f)) * (260 / 690f)).toInt()
            card_view.layoutParams = bannerParams
            mViewModel.getData()
            mViewModel.newsList.observe(this@HomeFragment, Observer { list ->
                var views = ArrayList<View>()
                list.forEach { bean ->
                    var view = LayoutInflater.from(activity).inflate(R.layout.news_item, null)
                    var tv = view.findViewById<TextView>(R.id.news_tv)
                    tv.text = bean.name
                    tv.setOnClickListener {
                        log(bean.link)
                        startRout(RoutUrl.Common.web_view, WebViewActivity.KEY_LOAD_URL, bean.link)
                    }
                    views.add(view)
                }
                news_view.setViews(views)
            })
            refresh_layout.isEnableLoadMore = false
            refresh_layout.setOnRefreshListener {
                mViewModel.getData()
                Handler().postDelayed({
                    refreshLayout.finishRefresh()
                }, 1000)
            }
            scan_img.setOnClickListener {
                requestPermissions()
                val intent = Intent(context, CaptureActivity::class.java)
                startActivityForResult(intent, 110)
            }
        }
    }

    /**
     * 注册消息接受者
     */
    fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction(MyReceiver.MESSAGE_RECEIVED_ACTION)
        LocalBroadcastManager.getInstance(XApplication.instance).registerReceiver(mMessageReceiver!!, filter)
    }

    /**
     * 内容接受者
     */
    inner class MessageReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (MyReceiver.MESSAGE_RECEIVED_ACTION == intent.action) {
                    val messge = intent.getStringExtra(MyReceiver.KEY_MESSAGE)
                    val extras = intent.getStringExtra(MyReceiver.KEY_EXTRAS)
                    val showMsg = StringBuilder()
                    showMsg.append("${MyReceiver.KEY_MESSAGE} : $messge\n")
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append("${MyReceiver.KEY_EXTRAS} : $extras\n")
                    }
//                    setCostomMsg(showMsg.toString())
                    log("push数据$showMsg")
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mViewModel.getUnReadNum()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getUnReadNum()
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.home_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!= Activity.RESULT_OK){
            return
        }
        if (requestCode == 110) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                val bundle = data.extras ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    try {
                        val scanCodeBean =
                            GsonUtil.toModel(AesEncryptUtils.aesDecrypt(result), ScanCodeBean::class.java)
                        val storeBean =
                            StoreBean(id = scanCodeBean.id, name = scanCodeBean.name, bond = scanCodeBean.bond)
                        startRout(RoutUrl.Near.pay_store, "bean", storeBean)
                    } catch (e: Exception) {
                        toast("非本平台店铺二维码")
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    toast("解析二维码失败")
                }
            }
        }
    }

    private fun requestPermissions() {
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (!EasyPermissions.hasPermissions(context, *perms)) {
            EasyPermissions.requestPermissions(this, "为保证APP正常使用，请允许相机等权限", 101, *perms)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(XApplication.instance).unregisterReceiver(mMessageReceiver!!)

    }
}