package com.wbg.xigui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.R
import com.wbg.xigui.bean.RefundSelectBean

/**
 * @author xyx
 * @date :2019/7/10 14:19
 */
abstract class PopRefundAdapter(
    var selectItem: RefundSelectBean? = null,
    var context: Context,
    var list: ArrayList<RefundSelectBean>
) : RecyclerView.Adapter<PopRefundAdapter.PopHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.refund_select_item, parent, false)
        return PopHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopHolder, position: Int) {
        list[position].apply {
            holder.run {
                nameTv.text = name
                checkTv.isSelected = selectItem?.name == name
                itemView.setOnClickListener { onItemClick(this@apply,position) }
            }

        }

    }

    class PopHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameTv = view.findViewById<TextView>(R.id.name_tv)!!
        val checkTv = view.findViewById<TextView>(R.id.check_tv)!!
    }

    abstract fun onItemClick(bean: RefundSelectBean,position: Int)
}