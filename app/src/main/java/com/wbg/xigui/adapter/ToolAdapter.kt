package com.wbg.xigui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.ToolBean

/**
 * @author tyk
 * @date :2019/9/10 9:26
 * @des : 我的  常用工具适配器
 */
class ToolAdapter : BaseQuickAdapter<ToolBean, BaseViewHolder>(R.layout.item_mine_tool) {
    override fun convert(helper: BaseViewHolder?, item: ToolBean?) {
        val drawable =mContext.resources.getDrawable(item!!.pic)
        helper?.getView<TextView>(R.id.tv_tool)?.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null)
        helper?.setText(R.id.tv_tool, item.title)



    }
}