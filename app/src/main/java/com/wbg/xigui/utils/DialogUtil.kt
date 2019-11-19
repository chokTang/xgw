package com.wbg.xigui.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.wbg.xigui.R
import com.wbg.xigui.bean.ConfirmSuccessBean

/**
 * @author xyx
 * @date :2019/6/24 15:46
 */
class DialogUtil {
    interface SelectRole {
        fun onSelect(roleType: Int)
    }

    companion object {
        fun showAllRole(context: Context, selectRole: SelectRole) {
            val view = LayoutInflater.from(context).inflate(R.layout.role_select_dialog, null)
            var dialog = Dialog(context)
            var one = view.findViewById<Button>(R.id.one)
            var two = view.findViewById<Button>(R.id.two)
            var three = view.findViewById<Button>(R.id.three)
            var four = view.findViewById<Button>(R.id.four)
            var views = arrayListOf<View>(one, two, three, four)
            views.forEachIndexed { index, view ->
                view.setOnClickListener {
                    selectRole.onSelect(index + 1)
                    dialog.cancel()
                }
            }
            dialog.setContentView(view)
            dialog.show()
            var window = dialog.window
            window.setBackgroundDrawableResource(R.color.transparent)
            var lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.width = (ResourcesUtil.getScreenWidth() * 0.8).toInt()
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }

        fun showRole(context: Context, currentRole: Int, selectRole: SelectRole) {
            val view = LayoutInflater.from(context).inflate(R.layout.role_select_dialog, null)
            var dialog = Dialog(context)
            var one = view.findViewById<Button>(R.id.one)
            var two = view.findViewById<Button>(R.id.two)
            var three = view.findViewById<Button>(R.id.three)
            var four = view.findViewById<Button>(R.id.four)
            var views = arrayListOf<View>(one, two, three, four)
            views[currentRole].visibility = View.GONE
            views.forEachIndexed { index, view ->
                view.setOnClickListener {
                    selectRole.onSelect(index + 1)
                    dialog.cancel()
                }
            }
            dialog.setContentView(view)
            dialog.show()
            var window = dialog.window
            window.setBackgroundDrawableResource(R.color.transparent)
            var lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.width = (ResourcesUtil.getScreenWidth() * 0.8).toInt()
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }

        fun showRightsInfo(context: Context, bean: ConfirmSuccessBean, sureAction: () -> Unit) {
            val view = LayoutInflater.from(context).inflate(R.layout.rights_info_dialog, null)
            var dialog = Dialog(context)
            val allNumTv = view.findViewById<TextView>(R.id.all_num_tv)
            val hasGetNumTv = view.findViewById<TextView>(R.id.has_get_num_tv)
            val restNumTv = view.findViewById<TextView>(R.id.rest_num_tv)
            val withdrawalNumTv = view.findViewById<TextView>(R.id.withdrawal_num_tv)
            val balanceNumTv = view.findViewById<TextView>(R.id.balance_num_tv)
            bean.run {
                allNumTv.text = "$bond"
                hasGetNumTv.text = "$bondInterestRate"
                restNumTv.text = "$surplusBond"
                withdrawalNumTv.text = "$moneyWithdrawal"
                balanceNumTv.text = "$balance"
            }

            view.findViewById<View>(R.id.btn_cancel).setOnClickListener {
                dialog.cancel()
            }
            view.findViewById<View>(R.id.sure_btn).setOnClickListener {
                dialog.cancel()
                sureAction()
            }
            dialog.setContentView(view)
            dialog.show()
            var window = dialog.window
            window.setBackgroundDrawableResource(R.color.transparent)
            var lp = window.attributes
            lp.gravity = Gravity.CENTER
            lp.width = (ResourcesUtil.getScreenWidth() * 0.8).toInt()
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }

        //网络加载弹窗
        private var dialog: Dialog? = null

        fun showLoading(text: String, context: Context, canCancel: Boolean = true) {
            val view = LayoutInflater.from(context).inflate(R.layout.http_dialog, null)
            val tv = view.findViewById<TextView>(R.id.content_tv)
            tv.text = text
            dialog = Dialog(context)
            dialog?.setContentView(view)
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setCancelable(canCancel)
            dialog?.show()
        }

        fun hideLoading() {
            dialog?.run {
                if (isShowing) {
                    cancel()
                }
            }
        }
    }

}