package com.wbg.xigui.ui.agent

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.AgentJoginViewModel
import kotlinx.android.synthetic.main.activity_agent_join.*
import toast

/**
 * @author tyk
 * @date :2019/9/10 11:20
 * @des : 代理商入驻 （成为代理）
 */
@Route(path = RoutUrl.Agent.join,extras = RoutUrl.Extra.login)
class AgentJoinActivity : BaseXActivity<AgentJoginViewModel>(),View.OnClickListener {

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("代理商入驻")
        setTitleBackground(resources.getColor(R.color.colorPrimary))
        setTitleTextColor(resources.getColor(R.color.white))
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
        addView(R.layout.activity_agent_join)
    }

    override fun initData() {
        setListener()
    }

    fun setListener(){
        rl_wx.setOnClickListener(this)
        rl_alipay.setOnClickListener(this)
        tv_agree.setOnClickListener(this)
        btn_join.setOnClickListener(this)
    }

    /**
     * 请求并显示明细相关数据
     */
    fun showDetailView(){


    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_wx->{//微信支付
                img_wx_select.isSelected = true
                img_alipay_select.isSelected = false
            }
            R.id.rl_alipay->{//支付宝支付
                img_wx_select.isSelected = false
                img_alipay_select.isSelected = true
            }
            R.id.tv_agree->{//点击同意

            }
            R.id.btn_join->{//点击成为代理商
                toast("成为代理商")
            }
        }
    }

}