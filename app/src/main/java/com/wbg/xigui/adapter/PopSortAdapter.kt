package com.wbg.xigui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.R
import com.wbg.xigui.utils.ResourcesUtil

/**
 * @author xyx
 * @date :2019/6/18 17:37
 */
abstract class PopSortAdapter(var selectedItem: String? = null, var context: Context, var list: ArrayList<String>) :
    RecyclerView.Adapter<PopSortAdapter.PopSortHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopSortHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.pop_sort_item, parent, false)
        return PopSortHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopSortHolder, position: Int) {
        holder.run {
            list[position].run {
                typeSortTv.text = this
                typeSortTv.setOnClickListener { onItemClick(position) }
                if (selectedItem == this) {
                    typeSortTv.setTextColor(ResourcesUtil.getColor(R.color.theme))
                } else {
                    typeSortTv.setTextColor(ResourcesUtil.getColor(R.color.text_black))
                }
            }
        }
    }

    class PopSortHolder(view: View) : RecyclerView.ViewHolder(view) {
        var typeSortTv = view.findViewById<TextView>(R.id.sort_type_tv)!!
    }

    abstract fun onItemClick(position: Int)
}