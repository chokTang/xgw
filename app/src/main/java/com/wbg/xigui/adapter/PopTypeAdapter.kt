package com.wbg.xigui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.R
import com.wbg.xigui.bean.StoreTypeBean
import com.wbg.xigui.utils.ResourcesUtil

/**
 * @author xyx
 * @date :2019/6/18 17:30
 */
abstract class PopTypeAdapter(var selectItem: StoreTypeBean? = null, var context: Context, var list: ArrayList<StoreTypeBean>) :
    RecyclerView.Adapter<PopTypeAdapter.PopHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.pop_type_item, parent, false)
        return PopHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopHolder, position: Int) {
        holder.run {
            list[position].run {
                typeTv.text = name
                typeTv.setOnClickListener { onItemClick(this) }
                if (selectItem == this) {
                    typeTv.setTextColor(ResourcesUtil.getColor(R.color.theme))
                } else {
                    typeTv.setTextColor(ResourcesUtil.getColor(R.color.text_black))
                }
            }

        }
    }

    class PopHolder(view: View) : RecyclerView.ViewHolder(view) {
        var typeTv = view.findViewById<TextView>(R.id.type_tv)!!
    }

    abstract fun onItemClick(bean: StoreTypeBean)
}