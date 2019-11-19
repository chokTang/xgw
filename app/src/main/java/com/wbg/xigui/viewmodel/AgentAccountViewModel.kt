package com.wbg.xigui.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.AccountBalanceBean
import com.wbg.xigui.bean.ListBean
import com.wbg.xigui.bean.MoneyRecordBean
import com.wbg.xigui.databinding.AgentAccountItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.ApplyWithdrawalActivity
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import startRout

/**
 * @author xyx
 * @date :2019/7/17 10:29
 */
class AgentAccountViewModel : BaseViewModel() {
    var bean = MutableLiveData<AccountBalanceBean>()
    var list = ObservableReplaceArrayList<MoneyRecordBean>()
    var binding = ItemBinding.of<MoneyRecordBean>(BR.bean, R.layout.agent_account_item)
    var adapter = (object : BindingRecyclerViewAdapter<MoneyRecordBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: MoneyRecordBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as AgentAccountItemBinding).run {
                item?.run {
                    typeTv.text = when (bondFlag) {
                        "1" -> "消费"
                        "2" -> "兑换"
                        "3" -> "提现"
                        else -> ""
                    }
                    timeTv.text = operationTime
                    amountTv.text = when (bondFlag) {
                        "1" -> "+$money"
                        "2" -> "+$money"
                        "3" -> "-$money"
                        else -> ""
                    }
                }
            }
        }
    })

    fun getData() {
        refresh()
        getBalance()
    }

    fun refresh() {
        mPage = 1
        list.clear()
        loadMore()
    }

    fun loadMore() {
        val map = HashMap<String, Any>()
        map["page"] = mPage
        map["size"] = 20
        service.getAccountRecord(map).netDispatch(object : RxNetObserver<ListBean<MoneyRecordBean>>() {
            override fun onError(msg: String) {
                changeLoadType(list, null, true)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: ListBean<MoneyRecordBean>?) {
                mPage++
                t?.run {
                    this@AgentAccountViewModel.list.addAll(list)
                }
                changeLoadType(this@AgentAccountViewModel.list, t?.list, false)
            }
        })
    }

    private fun getBalance() {
        service.getBalance(HashMap()).netDispatch(object : RxNetObserver<AccountBalanceBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: AccountBalanceBean?) {
                t?.run {
                    bean.value = this
                }
            }
        })
    }
    fun goApplyWithdrawal() {
        startRout(
            RoutUrl.Common.apply_withdrawal, ApplyWithdrawalActivity.KEY_BALANCE,bean.value
                ?.availableBalance?:0.0)
    }
}