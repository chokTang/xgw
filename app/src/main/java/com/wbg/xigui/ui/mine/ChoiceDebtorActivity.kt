package com.wbg.xigui.ui.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.ChoiceViewModel
import kotlinx.android.synthetic.main.activity_choice_debtor.*
import startRout

/**
 * @author tyk
 * @date :2019/9/9 16:27
 * @des : 选择债权人  还是债务人
 */
@Route(path = RoutUrl.Mine.choice, extras = RoutUrl.Extra.login)
class ChoiceDebtorActivity : BaseXActivity<ChoiceViewModel>(), View.OnClickListener {

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        addView(R.layout.activity_choice_debtor)
    }

    override fun initData() {
        setListener()
    }

    fun setListener() {
        ll_debtor.setOnClickListener(this)
        ll_creditor.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_debtor -> {//债务人
                startRout(RoutUrl.Common.debtor_confirm)
            }
            R.id.ll_creditor -> {//债权人
                startRout(RoutUrl.Common.creditor_confirm)
            }
        }
    }

}