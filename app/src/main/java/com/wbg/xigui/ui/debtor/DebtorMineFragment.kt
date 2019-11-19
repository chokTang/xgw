package com.wbg.xigui.ui.debtor

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
import com.wbg.xigui.databinding.DebtorMineFragmentBinding
import com.wbg.xigui.push.TagAliasOperatorHelper
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.ShareActivity
import com.wbg.xigui.utils.DialogUtil
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.viewmodel.DebtorMineViewModel
import kotlinx.android.synthetic.main.debtor_mine_fragment.*
import no
import startRout
import yes

/**
 * @author xyx
 * @date :2019/7/12 9:53
 * @des  债权人（我的）
 */
class DebtorMineFragment : BaseXFragment<DebtorMineViewModel>() {
    var binding: DebtorMineFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        mViewModel.bean.observe(this, Observer {
            it.run {
                name_tv.text = member?.userNcikName
                GlideUtil.loadHead(member?.userIcon ?: "", head_img, context!!)
            }
        })
        login_area.setOnClickListener {
            XApplication.isLogin().no {
                startRout(RoutUrl.Common.login)
            }
        }
        add_rights_ll.setOnClickListener {
            startRout(RoutUrl.Common.debtor_confirm)
        }
        change_role_ll.setOnClickListener {
            XApplication.isLogin().yes {
                DialogUtil.showRole(context!!, 1, object : DialogUtil.SelectRole {
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

        ll_share.setOnClickListener {
            startRout(RoutUrl.Mine.share, ShareActivity.KEY_TYPE,2)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mViewModel.getUserInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getUserInfo()
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.debtor_mine_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).titleBar(top_ll).init()
    }
}