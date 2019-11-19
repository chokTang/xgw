package com.wbg.xigui.ui.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.MsgBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.MsgDetailViewModel
import kotlinx.android.synthetic.main.msg_detail_activity.*

/**
 * @author xyx
 * @date :2019/7/9 17:17
 */
@Route(path = RoutUrl.Main.msg_detail)
class MsgDetailActivity : BaseXActivity<MsgDetailViewModel>() {
    var bean: MsgBean? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("消息详情")
        bean = intent.getSerializableExtra("bean") as MsgBean
        addView(R.layout.msg_detail_activity)
    }

    override fun initData() {
        bean?.run {
            mViewModel.setReaded(id ?: "")
            msg_content_tv.text = messageContent
        }

    }
}