package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.AccountListBean
import com.wbg.xigui.bean.AccountRightBean
import com.wbg.xigui.bean.MsgUnReadBean
import com.wbg.xigui.databinding.DebtorRepayItemBinding
import com.wbg.xigui.dialog.ConfirmDebtDialog
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.debtor.DebtorRepayFragment
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import com.wbg.xlib.net.RetrofitClient
import log
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import startRout

/**
 * @author xyx
 * @date :2019/7/18 9:41
 */
class DebtorRepayViewModel : BaseViewModel() {
    var context: DebtorRepayFragment?=null
    val newsNum = ObservableField<Int>()
    var list = ObservableReplaceArrayList<AccountRightBean>()
    var binding = ItemBinding.of<AccountRightBean>(BR.bean, R.layout.debtor_repay_item)
    var adapter = (object : BindingRecyclerViewAdapter<AccountRightBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: AccountRightBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as DebtorRepayItemBinding).run {
                item?.run {
                    seekBar.setEndText("$bond")
                    seekBar.setStartText("0")
                    val percent = (bond - surplusBond) / bond * 100

                    if (contractFlag != "-1") {
                        completeTv.visibility = View.VISIBLE
                        if (contractFlag=="0"||contractFlag=="1"){
                            completeTv.text = "完成结清合同"
                            ConfirmDebtDialog.newIntance().setType(1).
                                setUrl(RetrofitClient.h5 + "page/contract.html?bondId=" + id).
                                invoke(object : ConfirmDebtDialog.ClickListener{
                                    override fun click(v: View?) {
                                        sureContract(id!!)
                                    }
                                })
                                .show(context!!.fragmentManager,"yes_bottom_dialog")
                        }else{
                            completeTv.text = "查看结清合同"
                        }
                    } else {
                        completeTv.visibility = View.GONE
                    }

                    seekBar.setProgress(percent.toInt())
                    seekBar.setSeekbarText(StrUtil.subZeroAndDot(String.format("%.2f",bond - surplusBond)))
                }
            }
        }
    })


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

    /**
     * 账户债权信息列表
     */
    fun getData() {
        service.getRightList(HashMap()).netDispatch(object : RxNetObserver<AccountListBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {

            }

            override fun onSuccess(t: AccountListBean?) {
                t?.run {
                    this@DebtorRepayViewModel.list.replaceAll(list)
                }
            }
        })
    }


    fun goMsg() {
        startRout(RoutUrl.Main.msg)
    }


    /**
     * 获取未读消息条数
     */
    fun getUnReadNum() {
        val isLogin = !XApplication.instance.getUserInfo().token.isNullOrEmpty()
        if (!isLogin) {
            return
        }
        service.getUnreadMsgCount(HashMap()).netDispatch(object : RxNetObserver<MsgUnReadBean>() {
            override fun onError(msg: String) {
            }

            override fun onStart() {
            }

            override fun onSuccess(t: MsgUnReadBean?) {
                t?.run {
                    newsNum.set(unreadCount)
                }
            }
        })
    }
}