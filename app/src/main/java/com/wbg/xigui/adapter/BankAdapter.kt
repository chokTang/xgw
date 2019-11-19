package com.wbg.xigui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.R
import com.wbg.xigui.bean.BankBean

/**
 * @author xyx
 * @date :2019/7/18 16:30
 */
class BankAdapter(
    var context: Context,
    var list: ArrayList<BankBean>,
    var bean: BankBean? = null,
    var itemClick: (bean: BankBean) -> Unit
) :
    RecyclerView.Adapter<BankAdapter.BankHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.bank_item, parent, false)
        return BankHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BankHolder, position: Int) {
        holder.run {
            list[position].run {
                nameTv.text = bankName + accountNo
                nameTv.setOnClickListener { itemClick(this) }

            }
            bean?.run {
                nameTv.isSelected = accountNo == list[position].accountNo
            }
        }
    }


    class BankHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView = view.findViewById(R.id.bank_name_tv)
    }
}