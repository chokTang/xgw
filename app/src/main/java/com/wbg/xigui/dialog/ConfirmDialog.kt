package com.wbg.xigui.dialog

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.wbg.xigui.R
import kotlinx.android.synthetic.main.dialog_confirm.*

/**
 * @author tyk
 * @date :2019/9/2 10:59
 * @des : 提示dialog
 */
class ConfirmDialog : BaseDialogFragment(),View.OnClickListener {

    companion object{
        fun newIntance(): ConfirmDialog {
            val dialog = ConfirmDialog()
            return dialog
        }
    }
    var hints =""

    override fun getResId(): Any {
        return R.layout.dialog_confirm
    }

    fun setHint(hint:String):ConfirmDialog{
        hints = hint
        return this
    }

    override fun initView() {
        sure_btn.setOnClickListener(this)
    }

    override fun initData() {

        if (!TextUtils.isEmpty(hints)){
            tv_hint.text = hints
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.sure_btn->{
                clickListener?.click(v)
                dismiss()
            }
        }
    }

    override fun getViewWidth(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getViewHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getViewGravity(): Int {
        return Gravity.CENTER
    }

    override fun getAnimationType(): Int {
        return FORM_BOTTOM_TO_BOTTOM
    }


    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?): ConfirmDialog {
        this.clickListener = clickListener
        return this
    }

    interface ClickListener {
        fun click(v: View?)
    }

    var clickListener: ClickListener? = null
}