package com.wbg.xigui.ui.mine

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kw.rxbus.RxBus
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.AddressAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.X
import com.wbg.xigui.dialog.AlertDialogFragment
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.mine.AddAddressActivity.Companion.KEY_ADDRESS_BEAN
import com.wbg.xigui.ui.mine.AddAddressActivity.Companion.KEY_BUNDLE
import com.wbg.xigui.viewmodel.AddressViewModel
import kotlinx.android.synthetic.main.activity_address_list.*
import startRout

/**
 * @author tyk
 * @date :2019/8/16 10:40
 * @des : 地址列表
 */

@Route(path = RoutUrl.Mine.addressList)
class AddressActivity : BaseXActivity<AddressViewModel>(), View.OnClickListener {

    var adapter: AddressAdapter? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        addView(R.layout.activity_address_list)
        setListener()

        val linearLayoutManager = LinearLayoutManager(this)
        rv_address.layoutManager = linearLayoutManager
        adapter = AddressAdapter()
        rv_address.adapter = adapter
    }


    fun setListener() {
        ll_add_new_address.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()
        mViewModel.getAddressList {
            adapter?.setNewData(it.list)
            if (adapter?.getItem(0)!=null){
                RxBus.getInstance().send(adapter?.getItem(0))
            }
        }

    }

    override fun initData() {

        adapter?.onItemLongClickListener =
            BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
                val bean = adapter.data[position] as X
                AlertDialogFragment.newIntance()
                    .setKeyBackable(false)
                    .setCancleable(false)
                    .setCancleBtn(View.OnClickListener { })
                    .setSureBtn(View.OnClickListener {
                        mViewModel.deleteAddress(bean.id!!) {
                            adapter.remove(position)
                        }
                    })
                    .setContent("是否删除本条地址")
                    .show(this.supportFragmentManager, "deleteAddress")

                true
            }

        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as X
            val bundle = Bundle()
            bundle.putSerializable(KEY_ADDRESS_BEAN,bean)
            startRout(RoutUrl.Mine.add_address,KEY_BUNDLE, bundle)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_add_new_address -> {//添加新地址
                startRout(RoutUrl.Mine.add_address)
            }
            R.id.tv_confirm -> {//确定按钮
                finish()
            }
        }
    }
}