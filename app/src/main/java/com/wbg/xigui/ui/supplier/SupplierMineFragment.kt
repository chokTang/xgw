package com.wbg.xigui.ui.supplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.databinding.MineSupplierFragmentBinding
import com.wbg.xigui.push.TagAliasOperatorHelper
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.ShareActivity
import com.wbg.xigui.utils.DialogUtil
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.viewmodel.SupplierMineViewModel
import kotlinx.android.synthetic.main.mine_supplier_fragment.*
import no
import startRout
import yes

/**
 * @author xyx
 * @date :2019/7/11 9:48
 * @des:商家 （我的）
 */
class SupplierMineFragment : BaseXFragment<SupplierMineViewModel>(),View.OnClickListener {

    var binding: MineSupplierFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }
    override fun initImmersionBar() {
        ImmersionBar.with(this).titleBar(top_ll).init()
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.mine_supplier_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }


    override fun initData() {
        setListener()
        mViewModel.bean.observe(this, Observer {
            it.run {
                name_tv.text = member?.userNcikName
                money_tv.text = "￥$bond"
                GlideUtil.loadHead(member?.userIcon ?: "", head_img, context!!)
            }
        })
        login_area.setOnClickListener {
            XApplication.isLogin().no {
                startRout(RoutUrl.Common.login)
            }
        }
        change_role_ll.setOnClickListener {
            XApplication.isLogin().yes {
                DialogUtil.showRole(context!!, 3, object : DialogUtil.SelectRole {
                    override fun onSelect(roleType: Int) {
                        var role = ""
                        //设置别名
                        val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
                        tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
                        when (roleType) {
                            1 -> {//债权人
                                role = RoleType.creditor
                            }
                            2 ->{//债务人
                                role = RoleType.debtor
                            }
                            3 ->{//代理商
                                role = RoleType.agent
                            }
                            4 -> {//商家
                                role = RoleType.supplier
                            }
                        }
                        tagAliasBean.tags.add(role)
                        tagAliasBean.isAliasAction = false
                        TagAliasOperatorHelper.sequence++
                        TagAliasOperatorHelper.getInstance().handleAction(XApplication.instance, TagAliasOperatorHelper.sequence, tagAliasBean)
                        mViewModel.chooseRole(role)
                    }
                })
            }
        }
    }

    fun setListener(){
        ll_spread.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()
        mViewModel.getUserInfo()
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_spread->{
                startRout(RoutUrl.Mine.share, ShareActivity.KEY_TYPE,2)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mViewModel.getUserInfo()
        }
    }


}