package com.wbg.xigui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R

/**
 * @author tyk
 * @date :2019/9/2 10:15
 * @des :  债权兑换 条件适配器
 */
class ExchangeConditionAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_exchange_condition) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.tv_hint, (helper.adapterPosition+1).toString()+"."+item)
    }
}