package com.wbg.xigui.ui.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.BankAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.BankBean
import com.wbg.xigui.databinding.ApplyWithdrawalActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.ApplyWithdrawalViewModel
import kotlinx.android.synthetic.main.apply_withdrawal_activity.*
import no
import otherwise
import toast

/**
 * @author xyx
 * @date :2019/7/18 14:45
 */
@Route(path = RoutUrl.Common.apply_withdrawal)
class ApplyWithdrawalActivity : BaseXActivity<ApplyWithdrawalViewModel>(), View.OnClickListener {

    companion object {
        const val KEY_BALANCE = "balance"
    }

    var binding: ApplyWithdrawalActivityBinding? = null
    var bankBean: BankBean? = null
    var bankList: ArrayList<BankBean>? = null
    var payNum = 0.0
    private lateinit var selectDialog: BottomSheetDialog
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("申请提现")
        binding = bindLayout(R.layout.apply_withdrawal_activity)
    }

    override fun initData() {
        binding?.run {
            amount_edt.hint = "可提现金额：￥" + intent.getDoubleExtra(KEY_BALANCE, 0.0)
            bankTv.setOnClickListener {
                if (!bankList.isNullOrEmpty()) {
                    showBankDialog(bankList!!)
                } else {
                    mViewModel.getBankList {
                        bankList = it
                        showBankDialog(bankList!!)
                    }
                }
            }
        }
        amount_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var str: CharSequence = s
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        str = s.toString().subSequence(
                            0,
                            s.toString().indexOf(".") + 2 + 1
                        )
                        amount_edt.setText(str)
                        amount_edt.setSelection(str.length) //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0) == ".") {
                    str = "0$s"
                    amount_edt.setText(str)
                    amount_edt.setSelection(2)
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                    && s.toString().trim().length > 1
                ) {
                    if (s.toString().substring(1, 2) != ".") {
                        str = s.subSequence(0, 1)
                        amount_edt.setText(s.subSequence(0, 1))
                        amount_edt.setSelection(1)
                        return
                    }
                }
                val num = str.toString().isEmpty().no {
                    try {
                        str.toString().toDouble()
                    } catch (e: Exception) {
                        0.0
                    }
                }.otherwise { 0.0 }
                payNum = num
            }
        })
        mViewModel.getBankList { bankList = it }
        withdrawal_btn.setOnClickListener {
            if (payNum == 0.0) {
                toast("请输入提现金额")
                return@setOnClickListener
            }
            if (bankBean == null) {
                toast("请选择银行卡")
                return@setOnClickListener
            }
            mViewModel.apply(payNum, bankBean?.id ?: "") {
                finish()
            }
        }
        setListener()
    }

    fun setListener() {
        tv_all.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_all -> {
                amount_edt.text = Editable.Factory.getInstance().newEditable(intent.getDoubleExtra(KEY_BALANCE, 0.0).toString())
                payNum = intent.getDoubleExtra(KEY_BALANCE, 0.0)
            }
        }
    }


    fun showBankDialog(list: ArrayList<BankBean>) {
        selectDialog = BottomSheetDialog(this)
        var view = layoutInflater.inflate(R.layout.select_bank_dialog, null)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        view.findViewById<View>(R.id.add_tv).setOnClickListener {
            //            selectDialog.cancel()
            showDialog()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BankAdapter(this, list, bankBean) {
            selectDialog.cancel()
            bankBean = it
            bank_tv.isSelected = true
            account_tv.text = bankBean?.bankName + bankBean?.accountNo
        }
        view.findViewById<View>(R.id.close_img).setOnClickListener {
            selectDialog.cancel()
        }
        selectDialog.setContentView(view)
        selectDialog.setCancelable(false)
        selectDialog.show()
    }

    private fun showDialog() {
        var dialog = BottomSheetDialog(this)
        var view = layoutInflater.inflate(R.layout.add_bank_dialog, null)
        val bankNumEdt = view.findViewById<EditText>(R.id.bank_num_edt)
        val nameEdt = view.findViewById<EditText>(R.id.name_edt)
        val idCardEdt = view.findViewById<EditText>(R.id.id_card_edt)
        val phoneEdt = view.findViewById<EditText>(R.id.phone_edt)
        val sureBtn = view.findViewById<Button>(R.id.sure_btn)
        sureBtn.setOnClickListener {
            mViewModel.bindBank(
                bankNumEdt.text.toString(),
                idCardEdt.text.toString(),
                phoneEdt.text.toString(),
                nameEdt.text.toString()
            ) {
                selectDialog.cancel()
                dialog.cancel()
                bankBean = it[0]
                account_tv.text = bankBean?.bankName + bankBean?.accountNo
            }
        }
        view.findViewById<View>(R.id.close_img).setOnClickListener {
            dialog.cancel()
        }
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
    }
}