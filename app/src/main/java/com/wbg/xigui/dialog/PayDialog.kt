package com.wbg.xigui.dialog

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.wbg.xigui.R
import kotlinx.android.synthetic.main.activity_create_order.img_alipay_select
import kotlinx.android.synthetic.main.activity_create_order.img_wx_select
import kotlinx.android.synthetic.main.activity_create_order.rl_alipay
import kotlinx.android.synthetic.main.activity_create_order.rl_wx
import kotlinx.android.synthetic.main.dialog_pay.*

/**
 * @author tyk
 * @date :2019/8/29 16:10
 * @des :  支付弹出框
 */

class PayDialog : BaseDialogFragment(), View.OnClickListener {

    var type = 1  //阿里1  微信2
    companion object {
        fun newIntance(): PayDialog {
            val dialog = PayDialog()
            return dialog
        }
    }
    override fun getResId(): Any {
        return R.layout.dialog_pay
    }

    override fun initView() {
        rl_wx.setOnClickListener(this)
        rl_alipay.setOnClickListener(this)
        sure_btn.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_wx -> {//微信
                img_wx_select.isSelected = true
                img_alipay_select.isSelected = false
                type = 2
            }
            R.id.rl_alipay -> {//支付宝
                img_wx_select.isSelected = false
                img_alipay_select.isSelected = true
                type = 1
            }
            R.id.sure_btn->{//立即支付
                clickListener?.click(v,type)
            }
        }
    }

    override fun getViewWidth(): Int {
        return -1
    }

    override fun getViewHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getViewGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun getAnimationType(): Int {
        return FORM_BOTTOM_TO_BOTTOM
    }
    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?): PayDialog {
        this.clickListener = clickListener
        return this
    }

    interface ClickListener {
        fun click(v: View?,type:Int)
    }

    var clickListener: ClickListener? = null
}