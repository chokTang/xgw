package com.wbg.xigui.dialog

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.wbg.xigui.R
import kotlinx.android.synthetic.main.dialog_family_pay.*

/**
 * @author tyk
 * @date :2019/9/5 11:57
 * @des :
 */
class FamilyPayDialog : BaseDialogFragment(),View.OnClickListener {



    companion object{
        fun newIntance(): FamilyPayDialog {
            val dialog = FamilyPayDialog()
            return dialog
        }
    }
    override fun getResId(): Any {
        return R.layout.dialog_family_pay
    }

    override fun initView() {
        sure_btn.setOnClickListener(this)
    }

    override fun initData() {
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.sure_btn->{
                clickListener?.click(v,edt_phone.text.toString())
                dismiss()
            }
        }
    }

    override fun getViewWidth(): Int {
        return -1
    }

    override fun getViewHeight(): Int {
        return LinearLayout.LayoutParams.WRAP_CONTENT
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
    operator fun invoke(clickListener: ClickListener?): FamilyPayDialog {
        this.clickListener = clickListener
        return this
    }

    interface ClickListener {
        fun click(v: View?,phone:String?)
    }

    var clickListener: ClickListener? = null

}