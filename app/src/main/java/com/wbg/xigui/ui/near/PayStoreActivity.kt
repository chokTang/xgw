package com.wbg.xigui.ui.near

import android.Manifest
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.PaySuccessBean
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.DoubleUtil
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.viewmodel.PayStoreViewModel
import kotlinx.android.synthetic.main.pay_store_activity.*
import no
import otherwise
import startRout
import toast
import yes

/**
 * @author xyx
 * @date :2019/6/20 16:23
 */
@Route(path = RoutUrl.Near.pay_store, extras = RoutUrl.Extra.login)
class PayStoreActivity : BaseXActivity<PayStoreViewModel>() {
    var bean: StoreBean? = null
    var bond = 0.0f
    var payNum = 0.0
    var payType = 2//2微信支付1支付宝支付
    /**
     * 获取权限使用的 RequestCode
     */
    private val PERMISSIONS_REQUEST_CODE = 1002

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        requestPermission()
        bean = intent.getSerializableExtra("bean") as StoreBean
        setTitle(bean?.name)
        addView(R.layout.pay_store_activity)
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.need2success) {
            startRout(
                RoutUrl.Near.pay_store_success,
                "bean",
                PaySuccessBean((payType == 2).yes { "微信支付" }.otherwise { "支付宝支付" },
                    exchange_num_tv.text.toString(),
                    real_pay_num_tv.text.toString()
                )
            )
            mViewModel.need2success = false
            finish()
        }
    }

    override fun initData() {
        mViewModel.getBond(bean?.id ?: "") {
            it.run {
                bean?.id = id
                bean?.bond = bond
                bean?.name = name
                setTitle(bean?.name)
                exchange_tv.text = "本店兑换积分比例为：${StrUtil.subZeroAndDot(String.format("%.2f", (bond!! * 100)))}%"
            }
        }
        wechat_check_img.isSelected = true
        wechat_ll.setOnClickListener {
            payType = 2
            wechat_check_img.isSelected = true
            alipay_check_img.isSelected = false
        }
        alipay_ll.setOnClickListener {
            payType = 1
            wechat_check_img.isSelected = false
            alipay_check_img.isSelected = true
        }
        bond = bean?.bond ?: 0.0f
        exchange_tv.text = "本店兑换积分比例为：${StrUtil.subZeroAndDot(String.format("%.2f", (bond!! * 100)))}%"
        pay_num_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var str: CharSequence = s
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        str = s.toString().subSequence(
                            0,
                            s.toString().indexOf(".") + 2 + 1
                        )
                        pay_num_edt.setText(str)
                        pay_num_edt.setSelection(str.length) //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0) == ".") {
                    str = "0$s"
                    pay_num_edt.setText(str)
                    pay_num_edt.setSelection(2)
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                    && s.toString().trim().length > 1
                ) {
                    if (s.toString().substring(1, 2) != ".") {
                        str = s.subSequence(0, 1)
                        pay_num_edt.setText(s.subSequence(0, 1))
                        pay_num_edt.setSelection(1)
                        return
                    }
                }
                val num = str.toString().isEmpty().no {
                    try {
                        str.toString().toDouble()
                    } catch (e: Exception) {
                        0.0
                    }
                }.otherwise { 0.0 }
                payNum = num
                if (num > 0) {
                    user_right_num_tv.text = DoubleUtil.doubleFormatMaxDigits((bond * num), 2)
                    exchange_num_tv.text = "￥${DoubleUtil.doubleFormatMaxDigits(bond * num, 2)}"
                    real_pay_num_tv.text = "￥${DoubleUtil.doubleFormatMaxDigits(num, 2)}"
                    pay_btn.text = "确认买单￥${DoubleUtil.doubleFormatMaxDigits(num, 2)}"
                } else {
                    user_right_num_tv.text = "0.0"
                    exchange_num_tv.text = "￥0.0"
                    real_pay_num_tv.text = "￥0.0"
                    pay_btn.text = "确认买单￥$num"
                }
            }
        })
        pay_btn.setOnClickListener {
            //            if (payType == 2) {
//                toast("暂不支持微信支付")
//                return@setOnClickListener
//            }
            if (payNum > 0) {
                mViewModel.getPayParam(bean!!, payType, payNum)
//                startRout(RoutUrl.Near.pay_store_success)
            } else {
                toast("支付金额不能为0")
            }
        }
    }

    private fun requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_CODE
            )

        }
    }

    /**
     * 权限获取回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {

                // 用户取消了权限弹窗
                if (grantResults.isEmpty()) {
                    return
                }

                // 用户拒绝了某些权限
                for (x in grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        return
                    }
                }

                // 所需的权限均正常获取
//                showToast(this, getString(R.string.permission_granted))
            }
        }
    }
}