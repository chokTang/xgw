package com.wbg.xigui.ui.mine

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.TestStackAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.BankCardManageViewModel
import kotlinx.android.synthetic.main.activity_bankcard_manager.*

/**
 * @author tyk
 * @date :2019/9/6 14:28
 * @des :  银行卡管理
 */
@Route(path = RoutUrl.Mine.bankcard_manage, extras = RoutUrl.Extra.login)
class BankCardManageActivity : BaseXActivity<BankCardManageViewModel>() {


    var TEST_DATAS = arrayOf<Int>(
        R.color.color_1,
        R.color.color_2,
        R.color.color_3,
        R.color.color_4
    )

    var adapter: TestStackAdapter? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("银行卡管理")
        setRightText("添加")
        setRightColor(resources.getColor(R.color.dividerColor))
        addView(R.layout.activity_bankcard_manager)
    }

    override fun initData() {

        adapter = TestStackAdapter(this)
        stackview.setAdapter(adapter)

        mViewModel.getBankList {
            if (it.size>0){
                stackview.visibility = View.VISIBLE
                for (i in 0 until  it.size){
                    it[i].color = TEST_DATAS[i%4]
                }
                adapter?.updateData(it)
            }else{
                stackview.visibility = View.GONE
            }
        }

        adapter?.setClicklistener(object :TestStackAdapter.Clicklistener{
            override fun click(position: Int) {
            }
        })

        setRightAction {
           showDialog()
        }
    }


    /**
     * 添加银行卡
     */

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
                stackview.visibility = View.VISIBLE
                for (i in 0 until  it.size){
                    it[i].color = TEST_DATAS[i%4]
                }
                adapter?.updateData(it)
            }
        }
        view.findViewById<View>(R.id.close_img).setOnClickListener {
            dialog.cancel()
        }
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
    }

}