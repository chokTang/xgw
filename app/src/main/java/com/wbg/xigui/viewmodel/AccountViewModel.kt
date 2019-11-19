package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.*
import com.wbg.xigui.databinding.AccountRightsItemBinding
import com.wbg.xigui.databinding.AgentAccountItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.ApplyWithdrawalActivity
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import no
import otherwise
import startRout
import toast
import yes

/**
 * @author xyx
 * @date :2019/7/17 16:28
 */
class AccountViewModel : BaseViewModel() {
    var list = ObservableReplaceArrayList<AccountRightBean>()
    var recordList = ObservableReplaceArrayList<MoneyRecordBean>()
    var binding = ItemBinding.of<AccountRightBean>(BR.bean, R.layout.account_rights_item)
    var recordBinding = ItemBinding.of<MoneyRecordBean>(BR.bean, R.layout.agent_account_item)
    var recordAdapter = (object : BindingRecyclerViewAdapter<MoneyRecordBean>() {
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
    var bean = MutableLiveData<AccountBalanceBean>()
    var adapter = (object : BindingRecyclerViewAdapter<AccountRightBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: AccountRightBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as AccountRightsItemBinding).run {
                item?.run {
                    seekBar.setEndText("$bond")
                    seekBar.setStartText("0")
                    val percent = (bond - surplusBond) / bond * 100

                    if (contractFlag != "-1") {
                        completeTv.visibility = View.VISIBLE
                    } else {
                        completeTv.visibility = View.GONE
                    }

                    headPs.text = sure.yes { "已确权" }.otherwise { "未确权" }
                    visibleHead.yes { headPs.visibility = View.VISIBLE }
                        .otherwise { headPs.visibility = View.GONE }
                    seekBar.setProgress(percent.toInt())
                    seekBar.setSeekbarText(
                        StrUtil.subZeroAndDot(
                            String.format(
                                "%.2f",
                                bond - surplusBond
                            )
                        )
                    )
                    priorityTv.isSelected = seleted
                    priorityTv.setOnClickListener { seleted.no { setPriority(id ?: "") } }
                }

            }
        }
    })

    fun getData() {
        getRight()
        getBalance()
        refresh()
    }

    fun goApplyWithdrawal() {
        startRout(
            RoutUrl.Common.apply_withdrawal,
            ApplyWithdrawalActivity.KEY_BALANCE, bean.value?.availableBalance ?: 0.0
        )
    }

    private fun getRight() {
        service.getRightList(HashMap()).netDispatch(object : RxNetObserver<AccountListBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: AccountListBean?) {
                t?.run {
                    val mlist = ArrayList<AccountRightBean>()
                    if (sureList!!.size > 0) {
                        sureList.run {
                            forEachIndexed { index, bean ->
                                if (index == 0) {
                                    bean.visibleHead = true
                                    bean.seleted = true
                                }
                                bean.sure = true
                                mlist.add(bean)
                            }
                        }
                    }

                    if (noSureList!!.size > 0) {
                        noSureList.run {
                            this[0].seleted = true
                            this[0].visibleHead = true
                            mlist.addAll(this)
                        }
                    }
                    this@AccountViewModel.list.replaceAll(mlist)
                }
            }
        })
    }

    /**
     * 设置优先级
     */
    private fun setPriority(id: String) {
        val map = HashMap<String, Any>()
        map["bondId"] = id
        service.setPriority(map).netDispatch(object : RxNetObserver<AccountListBean>(true) {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: AccountListBean?) {
                t?.run {
                    val mlist = ArrayList<AccountRightBean>()
                    if (sureList?.size!! > 0) {
                        sureList.run {
                            forEachIndexed { index, bean ->
                                if (index == 0) {
                                    bean.visibleHead = true
                                    bean.seleted = true
                                }
                                bean.sure = true
                                mlist.add(bean)
                            }
                        }
                    }

                    if (noSureList?.size!! > 0) {
                        noSureList?.run {
                            this[0].seleted = true
                            this[0].visibleHead = true
                            mlist.addAll(this)
                        }
                    }

                    this@AccountViewModel.list.replaceAll(mlist)
                }
            }
        })
    }

    private fun getBalance() {
        service.getBalance(HashMap()).netDispatch(object : RxNetObserver<AccountBalanceBean>() {
            override fun onError(msg: String) {
                toast(msg)
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

    fun refresh() {
        mPage = 1
        list.clear()
        loadMore()
    }

    /**
     * 获取下面消费记录
     */
    fun loadMore() {
        val map = HashMap<String, Any>()
        map["page"] = mPage
        map["size"] = 20
        service.getAccountRecord(map)
            .netDispatch(object : RxNetObserver<ListBean<MoneyRecordBean>>() {
                override fun onError(msg: String) {
//                    changeLoadType(recordList, null, true)
                    errorMsg.value = msg
                }

                override fun onStart() {

                }

                override fun onSuccess(t: ListBean<MoneyRecordBean>?) {
                    mPage++
                    t?.run {
                        recordList.addAll(list)
                    }
//                    changeLoadType(recordList, t?.list, false)
                }
            })
    }
}