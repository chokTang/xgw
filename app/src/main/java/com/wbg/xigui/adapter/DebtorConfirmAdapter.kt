package com.wbg.xigui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.R
import com.wbg.xigui.bean.DebtorConfirmBean
import com.wbg.xigui.utils.StrUtil
import otherwise
import toast
import yes

/**
 * @author xyx
 * @date :2019/6/27 10:23
 */
class DebtorConfirmAdapter : RecyclerView.Adapter<DebtorConfirmAdapter.ViewHolder> {
    var list: ArrayList<DebtorConfirmBean>
    var context: Context
    var recyclerView: RecyclerView

    constructor(context: Context, recyclerView: RecyclerView, list: ArrayList<DebtorConfirmBean>) {
        this.list = list
        this.context = context
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.debtor_confirm_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            list[position].run {
                creditorNameEdt.setText(name)
                creditorIdNumEdt.setText(idCard)
                creditorPhoneEdt.setText(phone)
                if (position == 0) {
                    deleteTv.visibility = View.GONE
                } else {
                    deleteTv.visibility = View.VISIBLE
                }
                deleteTv.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确定要删除该条债务权人信息？")
                        .setPositiveButton("删除") { dialog, which ->
                            getListData(false)
                            list.removeAt(position)
                            recyclerView.isFocusableInTouchMode = false
                            notifyDataSetChanged()
                        }
                        .show()

                }
            }
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var creditorNameEdt = view.findViewById<EditText>(R.id.creditor_name_edt)
        var creditorIdNumEdt = view.findViewById<EditText>(R.id.creditor_id_num_edt)
        var creditorPhoneEdt = view.findViewById<EditText>(R.id.creditor_phone_edt)
        var deleteTv = view.findViewById<TextView>(R.id.delete_tv)
    }

    fun getListData(isAdd:Boolean): ArrayList<DebtorConfirmBean> {
        var manager = recyclerView.layoutManager!!
        for (i in 0 until (manager.childCount)) {
            var view = manager.getChildAt(i)!!
            var holder = recyclerView.getChildViewHolder(view) as ViewHolder
            holder.run {
                list[i].run {
                    name = creditorNameEdt.text.toString()
                    idCard = creditorIdNumEdt.text.toString()
                    phone = creditorPhoneEdt.text.toString()
                    if(isAdd) {
                        if (creditorNameEdt.text.toString().isEmpty()) {
                            toast("请输入债权人姓名")
                            return ArrayList()
                        }
                        if (!StrUtil.isIDCard(creditorIdNumEdt.text.toString())) {
                            toast("请输入正确的债权人身份证号码")
                            creditorIdNumEdt.error = "请输入正确的债权人身份证号码"
                            return ArrayList()
                        }
                        if (!StrUtil.isMobileNo(creditorPhoneEdt.text.toString())) {
                            toast("请输入债权人手机号码")
                            creditorPhoneEdt.error = "请输入债权人手机号码"
                            return ArrayList()
                        }
                    }
                }

            }
        }
        return list
    }

    fun addInfo() {
        getListData(true).isEmpty().yes {
            return
        }.otherwise {
            list.add(DebtorConfirmBean())
            recyclerView.isFocusableInTouchMode = false
            notifyDataSetChanged()
        }
    }

    fun getResult(): ArrayList<DebtorConfirmBean> {
        var manager = recyclerView.layoutManager!!
        for (i in 0 until (manager.childCount)) {
            var view = manager.getChildAt(i)!!
            var holder = recyclerView.getChildViewHolder(view) as ViewHolder
            holder.run {
                list[i].run {
                    if (creditorNameEdt.text.toString().isEmpty()) {
                        toast("请输入债权人姓名")
                        return ArrayList()
                    }
                    if (!StrUtil.isIDCard(creditorIdNumEdt.text.toString())) {
                        toast("请输入正确的债权人身份证号码")
                        creditorIdNumEdt.error = "请输入正确的债权人身份证号码"
                        return ArrayList()
                    }
                    if (!StrUtil.isMobileNo(creditorPhoneEdt.text.toString())) {
                        toast("请输入债权人手机号码")
                        creditorPhoneEdt.error = "请输入债权人手机号码"
                        return ArrayList()
                    }
                    name = creditorNameEdt.text.toString()
                    idCard = creditorIdNumEdt.text.toString()
                    phone = creditorPhoneEdt.text.toString()
                }

            }
        }
        return list
    }
}