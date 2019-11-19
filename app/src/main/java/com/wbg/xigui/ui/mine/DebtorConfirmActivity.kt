package com.wbg.xigui.ui.mine

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.DebtorConfirmAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.DebtorConfirmBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.viewmodel.DebtorConfirmViewModel
import com.wbg.xigui.widget.AndroidBug5497Workaround
import kotlinx.android.synthetic.main.debtor_confirm_activity.*
import log
import startRout
import toast

/**
 * @author xyx
 * @date :2019/6/27 10:34
 */
@Route(path = RoutUrl.Common.debtor_confirm)
class DebtorConfirmActivity : BaseXActivity<DebtorConfirmViewModel>(),View.OnClickListener {


    var adapter: DebtorConfirmAdapter? = null
    var selected = true

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidBug5497Workaround.assistActivity(this)
    }

    override fun initView() {
        addView(R.layout.debtor_confirm_activity)
    }

    override fun initData() {
        setListener()
        tv_check.isSelected = selected
        setTitle("确权")
        setRightColor(ResourcesUtil.getColor(R.color.text_black))
        setRightText("添加确权资料")
        setRightAction {
            adapter?.addInfo()
        }
        adapter = DebtorConfirmAdapter(this, recyclerView, arrayListOf(DebtorConfirmBean()))
        recyclerView.adapter = adapter
        sure.setOnClickListener {
            log(adapter?.getResult().toString())
            if (StrUtil.isEmpty(debtor_name_edt.text.toString())) {
                toast("请输入债务人姓名")
                debtor_name_edt.error = "请输入债务人姓名"
                return@setOnClickListener
            }
            if (!StrUtil.isIDCard(debtor_id_num_edt.text.toString())) {
                toast("请输入正确的债务人身份证号码")
                debtor_id_num_edt.error = "请输入正确的债务人身份证号码"
                return@setOnClickListener
            }
            if (!selected){
                toast("请同意《兑换解债协议》")
                return@setOnClickListener
            }
            mViewModel.postInfo(
                debtor_name_edt.text.toString(),
                debtor_id_num_edt.text.toString(),
                adapter?.getResult() ?: ArrayList()
            ) {
                startRout(RoutUrl.Debtor.home)
                XApplication.instance.clearActivity()
            }
        }
    }

    fun setListener(){
        tv_check.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_check->{
                selected = !selected
                tv_check.isSelected = selected
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        XApplication.instance.setRole(null)
    }
}