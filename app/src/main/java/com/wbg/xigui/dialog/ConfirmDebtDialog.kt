package com.wbg.xigui.dialog

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.wbg.xigui.R
import com.wbg.xigui.utils.WebViewUtils
import kotlinx.android.synthetic.main.dialog_confirm_debt.*

/**
 * @author tyk
 * @date :2019/9/4 16:33
 * @des : 债权结清确认dialog
 */
class ConfirmDebtDialog : BaseDialogFragment(),View.OnClickListener {

    var loadUrl = "about:blank"
    var type = 0  //0  是没有下面按钮bottom  1  是有下面bottom

    companion object {
        fun newIntance(): ConfirmDebtDialog {
            val dialog = ConfirmDebtDialog()
            return dialog
        }
    }

    override fun getResId(): Any {
        return R.layout.dialog_confirm_debt
    }

    fun setUrl(url: String): ConfirmDebtDialog {
        loadUrl = url
        return this
    }
    fun setType(mtype: Int): ConfirmDebtDialog {
        type = mtype
        return this
    }

    override fun initView() {
        WebViewUtils.webviewSet(webview)
        webview.loadUrl(loadUrl)
        if (type == 0){
            ll_bottom.visibility = View.GONE
        }else{
            ll_bottom.visibility = View.VISIBLE
        }
    }

    override fun initData() {
    }

    fun setListener(){
        tv_cancel.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_confirm->{//确定
                clickListener?.click(v)
                dismiss()
            }
            R.id.tv_cancel->{//取消
                dismiss()
            }
        }
    }


    override fun getViewWidth(): Int {
        return LinearLayout.LayoutParams.MATCH_PARENT
    }

    override fun getViewHeight(): Int {
        return LinearLayout.LayoutParams.WRAP_CONTENT
    }

    override fun getViewGravity(): Int {
        return Gravity.CENTER
    }

    override fun getAnimationType(): Int {
        return FORM_LEFT_TO_LEFT
    }


    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?): ConfirmDebtDialog {
        this.clickListener = clickListener
        return this
    }

    interface ClickListener {
        fun click(v: View?)
    }

    var clickListener: ClickListener? = null
}