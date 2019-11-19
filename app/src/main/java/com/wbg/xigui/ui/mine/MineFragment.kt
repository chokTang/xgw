package com.wbg.xigui.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.ToolAdapter
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.bean.ToolBean
import com.wbg.xigui.databinding.MineFragmentBinding
import com.wbg.xigui.push.TagAliasOperatorHelper
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.mine_fragment.*
import no
import startRout
import yes

/**
 * @author xyx
 * @date :2019/6/18 9:50
 */
class MineFragment : BaseXFragment<MineViewModel>() {

    companion object{
        const val TITLE_1 = "债权兑换"
        const val TITLE_2 = "新增债权"
        const val TITLE_3= "亲情付"
        const val TITLE_5= "地址管理"
        const val TITLE_6= "银行卡管理"
        const val TITLE_7= "邀请分享"
        const val TITLE_8 = "成为代理"
        const val TITLE_9 = "成为商家"
        const val TITLE_10= "偿还进度"
        const val TITLE_11= "代理收益"
        const val TITLE_12= "我的店铺"
    }

    val picList = arrayOf(
        R.drawable.icon_zqdh,
        R.drawable.icon_xzzq,
        R.mipmap.icon_cwdl,
        R.mipmap.icon_cwsj,
        R.mipmap.icon_chjd,
        R.drawable.icon_qqf,
        R.drawable.icon_dzgl,
        R.mipmap.icon_card_of_bank,
        R.mipmap.icon_share
    )
    val titleLisr = arrayOf(
        TITLE_1, TITLE_2,TITLE_8,TITLE_9,TITLE_10, TITLE_3, TITLE_5
        , TITLE_6, TITLE_7
    )

    val toolList:MutableList<ToolBean> = arrayListOf()
    var binding: MineFragmentBinding? = null
    var adapter: ToolAdapter? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        val gridLayoutManager = GridLayoutManager(activity, 4)
        rv_tool.layoutManager = gridLayoutManager
        adapter= ToolAdapter()
        rv_tool.adapter = adapter

        for (i in 0 until picList.size){
            val bean =ToolBean(picList[i],titleLisr[i])
            toolList.add(bean)
        }
        if (XApplication.instance.getUserInfo().roles!!.contains(RoleType.agent)) {//有代理商  将成为代理改成代理收益
            val bean =ToolBean(R.mipmap.icon_cwdl,TITLE_11)
            toolList[2] = bean
        }

        if (XApplication.instance.getUserInfo().roles!!.contains(RoleType.supplier)) {//有商家  将成为商家改成我的店铺
            val bean =ToolBean(R.mipmap.icon_cwsj,TITLE_12)
            toolList[3] = bean
        }
        //有债务人角色才显示 偿还进度  没有就移除该项
        if (!XApplication.instance.getUserInfo().roles!!.contains(RoleType.debtor)){
            toolList.removeAt(4)
        }
        adapter?.setNewData(toolList)

        XApplication.isLogin()
            .yes { name_tv.text = XApplication.instance.getUserInfo().member?.userNcikName ?: "" }

        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ToolBean
            when(bean.title){
                TITLE_1->{//债权兑换
                    mViewModel.goExchange()
                }
                TITLE_2->{//新增债权
                    mViewModel.addRights()
                }
                TITLE_3->{//亲情付
                    mViewModel.familyPay()
                }

                TITLE_5->{//地址管理
                    mViewModel.goAddress()
                }
                TITLE_6->{//银行卡管理
                    mViewModel.goBankManager()
                }
                TITLE_7->{//推广分享
                    mViewModel.goShare()
                }
                TITLE_8->{//成为代理
                    startRout(RoutUrl.Agent.join)
                }
                TITLE_9->{//成为商家
                    startRout(RoutUrl.Mine.store_join)
                }
                TITLE_10->{//偿还进度  就是跳转到 债务人角色
                    var role = ""
                    val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
                    tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
                    role = RoleType.debtor
                    tagAliasBean.tags.add(role)
                    tagAliasBean.isAliasAction = false
                    TagAliasOperatorHelper.sequence++
                    TagAliasOperatorHelper.getInstance().handleAction(
                        XApplication.instance,
                        TagAliasOperatorHelper.sequence,
                        tagAliasBean
                    )
                    mViewModel.chooseRole(role)
                }
                TITLE_11->{//代理收益 就是跳转到 代理商角色
                    var role = ""
                    val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
                    tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
                    role = RoleType.agent
                    tagAliasBean.tags.add(role)
                    tagAliasBean.isAliasAction = false
                    TagAliasOperatorHelper.sequence++
                    TagAliasOperatorHelper.getInstance().handleAction(
                        XApplication.instance,
                        TagAliasOperatorHelper.sequence,
                        tagAliasBean
                    )
                    mViewModel.chooseRole(role)
                }
                TITLE_12->{//我的店铺  就是跳转到 商家角色
                    var role = ""
                    val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
                    tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
                    role = RoleType.supplier
                    tagAliasBean.tags.add(role)
                    tagAliasBean.isAliasAction = false
                    TagAliasOperatorHelper.sequence++
                    TagAliasOperatorHelper.getInstance().handleAction(
                        XApplication.instance,
                        TagAliasOperatorHelper.sequence,
                        tagAliasBean
                    )
                    mViewModel.chooseRole(role)
                }
            }
        }

        login_area.setOnClickListener {
            XApplication.isLogin().no { startRout(RoutUrl.Common.login) }
        }
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindLayout(inflater, R.layout.mine_fragment, container)
        binding?.run {
            model = mViewModel
            mViewModel.bean.observe(this@MineFragment, Observer {
                it.run {
                    all_rights_tv.text = "$bond"
                    withdrawal_rights_tv.text = "$bondWithdrawal"
                    surplus_rights_tv.text = "$surplusBond"
                    money_withdrawal_tv.text = "$moneyWithdrawal"
                    balance_tv.text = "$balance"
                    GlideUtil.loadHead(member?.userIcon ?: "", head_img, context!!)
                    name_tv.text = member?.userNcikName
                }
            })
        }
        return mRootView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            showAccountView()
        }
    }

    override fun onResume() {
        super.onResume()
        showAccountView()
    }

    fun showAccountView() {
        mViewModel.getUserInfo()
//        if (XApplication.instance.getUserInfo().flag == "1") {//新用户
//            ll_account.visibility = View.GONE
//        } else {//老用户
//            if (null != XApplication.instance.getUserInfo().roles) {
//                if (XApplication.instance.getUserInfo().roles!!.contains(RoleType.creditor)) {
//                    ll_account.visibility = View.VISIBLE
//                } else {
//                    ll_account.visibility = View.GONE
//                }
//            } else {
//                ll_account.visibility = View.GONE
//            }
//        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
    }
}