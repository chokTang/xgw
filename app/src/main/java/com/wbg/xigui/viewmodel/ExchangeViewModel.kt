package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.ExchangeConditionAdapter
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ExchangeBean
import com.wbg.xigui.bean.ExchangeBeanList
import com.wbg.xigui.databinding.RightsExchangeItemBinding
import com.wbg.xigui.dialog.ConfirmDialog
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.ui.mine.ExchangeActivity
import com.wbg.xigui.widget.DividerGridItemDecoration
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import toast

/**
 * @author xyx
 * @date :2019/6/27 11:13
 */
class ExchangeViewModel : BaseViewModel() {
    var context: ExchangeActivity? = null
    val binding = ItemBinding.of<ExchangeBeanList>(BR.bean, R.layout.rights_exchange_item)
    var conditionAdapter: ExchangeConditionAdapter? = null
    val adapter = (object : BindingRecyclerViewAdapter<ExchangeBeanList>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: ExchangeBeanList?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as RightsExchangeItemBinding).run {
                val linearLayoutManager = LinearLayoutManager(XApplication.currentActivity)
                rvCondition.layoutManager = linearLayoutManager
                conditionAdapter = ExchangeConditionAdapter()
                rvCondition.adapter = conditionAdapter
                rvCondition.addItemDecoration(
                    DividerGridItemDecoration(
                        XApplication.currentActivity,
                        R.drawable.listdivider_white_10
                    )
                )
                conditionAdapter?.setNewData(item!!.condition)
                exchangeBtn.setOnClickListener {
                    checkIsCanExchange(item?.id!!)
                }
            }
        }
    })
    val exchangelist = ObservableReplaceArrayList<ExchangeBeanList>()

    /**
     * 获取债权列表
     */
    fun getData() {
        val map = HashMap<String, Any>()
        service.getExchangeList(map).netDispatch(object : RxNetObserver<ExchangeBean>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: ExchangeBean?) {
                t?.run {
                    exchangelist.replaceAll(this.list)
                }
            }
        })
    }

    /**
     * 查看是否可以兑换
     */
    fun checkIsCanExchange(exchangeId:String) {
        val map = HashMap<String, Any>()
        map["exchangeId"] = exchangeId
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.checkIsCanExchange(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: Any?) {
                showDialog(exchangeId)
            }
        })
    }

    /**
     *  兑换
     */
    fun exchange(exchangeId:String) {
        val map = HashMap<String, Any>()
        map["exchangeId"] = exchangeId
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.exchange(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: Any?) {

            }
        })
    }

    /**
     * 显示提示对话框
     */
    fun showDialog(exchangeId:String) {
        ConfirmDialog.newIntance().invoke(object : ConfirmDialog.ClickListener {
            override fun click(v: View?) {
                exchange(exchangeId)
            }
        }).show(context!!.supportFragmentManager, "show_confirm_dialog")
    }

}