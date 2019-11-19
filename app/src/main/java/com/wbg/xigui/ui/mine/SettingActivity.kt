package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.dialog.AlertDialogFragment
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.AppUtil
import com.wbg.xigui.utils.GlideUtil
import kotlinx.android.synthetic.main.setting_creditor_activity.*
import log
import netDispatch
import startRout
import toast
import yes

/**
 * @author xyx
 * @date :2019/7/5 11:10
 */
@Route(path = RoutUrl.Mine.setting)
class SettingActivity : BaseXActivity<BaseViewModel>(), View.OnClickListener {


    val isLogin = XApplication.isLogin()
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("设置")
        addView(R.layout.setting_creditor_activity)
        setListener()
    }

    fun setListener() {
        clear_cache_ll.setOnClickListener(this)
        about_ll.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        tv_cache.text = AppUtil.getCache(this)
        version_tv.text = "V${AppUtil.getVersionName()}"
        isLogin.yes {
            log_out.visibility = View.VISIBLE
            XApplication.instance.getUserInfo().run {
                GlideUtil.loadImg(member?.userIcon ?: "", head_img, this@SettingActivity)
                nick_name_tv.text = member?.userNcikName
                val phone = member?.userPhone ?: ""
                try {
                    phone_tv.text =
                        phone.substring(0, 3) + "****" + phone.substring(phone.length - 3)
                } catch (e: Exception) {
                    log("手机号有问题")
                }
            }
        }
        log_out.setOnClickListener {
            service.logout(HashMap()).netDispatch(object : RxNetObserver<Any>() {
                override fun onError(msg: String) {
                }

                override fun onStart() {
                }

                override fun onSuccess(t: Any?) {
                    AlertDialogFragment.newIntance()
                        .setTitle("提示")
                        .setContent("确定退出当前账号吗?")
                        .setCancleBtn { }
                        .setSureBtn {
                            XApplication.instance.logout()
                            startRout(RoutUrl.Common.login)
                            XApplication.instance.clearActivity()
                        }
                        .show(supportFragmentManager, "logout")
                }
            })

        }
        edt_btn.setOnClickListener {
            startRout(RoutUrl.Mine.person_info)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clear_cache_ll -> {
                if (AppUtil.getCache(this).contains("0.0")) {
                    toast("暂无缓存")
                    return
                }
                AlertDialogFragment.newIntance()
                    .setTitle("提示")
                    .setContent("清除缓存?")
                    .setCancleBtn { }
                    .setSureBtn {
                        AppUtil.clearAllCache(this)
                        tv_cache.text = AppUtil.getCache(this)
                    }
                    .show(supportFragmentManager, "clearCache")
            }

            R.id.about_ll -> {//关于我们
                startRout(RoutUrl.Mine.about_us)
            }

        }
    }
}