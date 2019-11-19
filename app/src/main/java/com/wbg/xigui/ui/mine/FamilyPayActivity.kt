package com.wbg.xigui.ui.mine

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.AvatarAdapter
import com.wbg.xigui.adapter.PayRecordAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.AvatarBeanList
import com.wbg.xigui.dialog.AlertDialogFragment
import com.wbg.xigui.dialog.FamilyPayDialog
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.FamilyPayViewModel
import com.wbg.xigui.widget.DividerGridItemDecoration
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.activity_help_pay.*
import startRout
import toast

/**
 * @author tyk
 * @date :2019/9/5 9:57
 * @des :  亲情付
 */

@Route(path = RoutUrl.Mine.family_pay)
class FamilyPayActivity : BaseXActivity<FamilyPayViewModel>() {

    var adapter: AvatarAdapter? = null
    var recordAdapter: PayRecordAdapter? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        setTitle("亲情付")
        setTitleBackground(resources.getColor(R.color.colorPrimary))
        setTitleTextColor(resources.getColor(R.color.white))
        setRightTextColor(resources.getColor(R.color.white))
        setRightText("分享码")
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
        addView(R.layout.activity_help_pay)

        setRightAction {
            startRout(RoutUrl.Mine.share, ShareActivity.KEY_TYPE,1)
        }
    }

    override fun initData() {
        val linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rv_person.layoutManager = linearLayoutManager
        adapter = AvatarAdapter()
        rv_person.adapter = adapter
        rv_person.addItemDecoration(DividerGridItemDecoration(this, R.drawable.listdivider_white_10))
        mViewModel.getMenberList {
            val bean  = AvatarBeanList()
            bean.type = 0
            it.list!!.add(0,bean)
            adapter?.setNewData(it.list)
        }

        val linearLayoutManager1 = LinearLayoutManager(this)
        rv_record.layoutManager = linearLayoutManager1
        recordAdapter = PayRecordAdapter()
        rv_record.adapter = recordAdapter

        loadViewHelper = (object : LoadViewHelper(this@FamilyPayActivity) {
            override fun action() {
                mViewModel.refresh {
                    recordAdapter?.setNewData(it)
                }
            }
        })
        loadViewHelper?.setLayoutId(R.layout.empty_layout)
        mViewModel.getRecordList{
            recordAdapter?.setNewData(it)
        }

        refresh_layout.setOnRefreshListener {
            mViewModel.refresh{
                recordAdapter?.setNewData(it)
            }
        }
        refresh_layout.setOnLoadMoreListener {
            mViewModel.getRecordList{
                recordAdapter?.setNewData(it)
            }
        }


        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean =  adapter.data[position] as AvatarBeanList
            if (bean.type==0){//添加
                FamilyPayDialog.newIntance().invoke(object :FamilyPayDialog.ClickListener{
                    override fun click(v: View?, phone: String?) {
                        mViewModel.addMember(phone!!){
                            toast("添加成功")
                            mViewModel.getMenberList {
                                val bean  = AvatarBeanList()
                                bean.type = 0
                                it.list!!.add(0,bean)
                                adapter?.setNewData(it.list)
                            }
                        }
                    }
                }).show(supportFragmentManager,"family_pay_dialog")
            }else{//点击头像

            }
        }


        adapter?.setOnItemLongClickListener { adapter, view, position ->
            val bean =  adapter.data[position] as AvatarBeanList
            AlertDialogFragment.newIntance().setTitle("提示")
                .setContent("确认删除当前成员!")
                .setCancleBtn {

                }
                .setSureBtn {
                    if (bean.type!=0){
                        mViewModel.deleteMember(bean.id!!){
                            adapter.remove(position)
                        }
                    }
                }.show(supportFragmentManager,"confirm_dialog")
            true
        }
    }

}