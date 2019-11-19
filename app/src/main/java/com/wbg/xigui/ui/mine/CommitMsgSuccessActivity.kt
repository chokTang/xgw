package com.wbg.xigui.ui.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.CommitSuccessViewModel
import kotlinx.android.synthetic.main.activity_store_commit_success.*
import toast

/**
 * @author tyk
 * @date :2019/9/16 11:40
 * @des :  信息提交成功
 */
@Route(path = RoutUrl.Mine.commit_success,extras = RoutUrl.Extra.login)
class CommitMsgSuccessActivity : BaseXActivity<CommitSuccessViewModel>() ,View.OnClickListener{

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("信息提交成功")
        addView(R.layout.activity_store_commit_success)
    }

    override fun initData() {
        setListener()
    }

    fun setListener(){
        btn_go_shop_msg.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_go_shop_msg->{
                toast("店铺信息提交成功")
            }
        }
    }

}