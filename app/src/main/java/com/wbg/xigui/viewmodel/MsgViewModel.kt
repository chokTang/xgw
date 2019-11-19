package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ViewDataBinding
import com.hotokay.jakeefactory.ui.base.GsonUtil
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.ListBean
import com.wbg.xigui.bean.MsgBean
import com.wbg.xigui.dialog.ConfirmDebtDialog
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.push.PushBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.WebViewActivity
import com.wbg.xigui.ui.main.MsgActivity
import com.wbg.xigui.ui.mine.OrderActivity.Companion.KEY_TYPE_POSITION
import com.wbg.xigui.widget.ObservableReplaceArrayList
import com.wbg.xlib.net.RetrofitClient
import log
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import startRout
import toast

/**
 * @author xyx
 * @date :2019/6/27 11:34
 */
class MsgViewModel : BaseViewModel() {
    var context: MsgActivity? = null
    val list = ObservableReplaceArrayList<MsgBean>()
    val binding = ItemBinding.of<MsgBean>(BR.bean, R.layout.msg_item)
    val adapter = (object : BindingRecyclerViewAdapter<MsgBean>() {
        override fun onBindBinding(
                binding: ViewDataBinding,
                variableId: Int,
                layoutRes: Int,
                position: Int,
                item: MsgBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            binding.run {
                val bean = GsonUtil.toModel(item?.jumpParams, PushBean::class.java)
                root.setOnClickListener {
                    setReaded(item?.id!!)
                    when (item.jumpType) {
                        "2" -> {//-h5跳转
                            startRout(RoutUrl.Common.web_view, WebViewActivity.KEY_LOAD_URL, item.jumpParams.toString())
                        }
                        "11" -> {//-债务人结清合同
                            ConfirmDebtDialog.newIntance().setType(1).
                                    setUrl(RetrofitClient.h5 + "page/contract.html?bondId=" + bean.bondId).
                                    invoke(object :ConfirmDebtDialog.ClickListener{
                                        override fun click(v: View?) {
                                            sureContract(bean.bondId!!)
                                        }
                                    })
                                .show(context!!.supportFragmentManager,"yes_bottom_dialog")

                        }
                        "12" -> {//-债权人结清合同 (无按钮)
                            ConfirmDebtDialog.newIntance().setType(0).
                                    setUrl(RetrofitClient.h5 + "page/contract.html?bondId=" + bean.bondId).
                                    show(context!!.supportFragmentManager,"no_bottom_dialog")
                        }
                        "13", "15" -> {//商品未审核通过 //-商品审核通过   商家   暂无操作
                        }
                        "14" -> {//-商品售罄  商家  暂无操作
                        }
                        "16" -> {//订单已付款  商家  暂无操作
                        }
                        "17" -> {//订单已发货（债权人）  跳入订单页面
                            startRout(RoutUrl.Mine.order, KEY_TYPE_POSITION, 2)
                        }
                        "18" -> {//订单退货（商家）  暂无操作
                        }
                        "19" -> {//订单退货处理情况（债权人）
                            startRout(RoutUrl.Mine.order, KEY_TYPE_POSITION, 1)
                        }
                    }
                }
            }
        }
    })

    fun getData() {
        val map = HashMap<String, Any>()
        map["page"] = mPage
        map["size"] = 20
        service.getMsgList(map).netDispatch(object : RxNetObserver<ListBean<MsgBean>>() {
            override fun onError(msg: String) {
                toast(msg)
                changeLoadType(list, null, true)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: ListBean<MsgBean>?) {
                mPage++
                t?.run {
                    if (list != null) {
                        this@MsgViewModel.list.addAll(list)
                    }
                    changeLoadType(this@MsgViewModel.list, list, false)
                }
            }
        })
    }


    fun setReaded(id: String) {
        val map = HashMap<String, Any>()
        map["id"] = id
        service.setReaded(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                log(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
            }
        })
    }

    /**
     * 确认合同信息
     */
    fun sureContract(id: String) {
        val map = HashMap<String, Any>()
        map["bondId"] = id
        map["memberId"] = XApplication.instance.getMemberId().toString()
        service.sureContract(map).netDispatch(object : RxNetObserver<Any>() {
            override fun onError(msg: String) {
                log(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: Any?) {
            }
        })
    }

    fun refresh() {
        list.clear()
        mPage = 1
        getData()
    }
}