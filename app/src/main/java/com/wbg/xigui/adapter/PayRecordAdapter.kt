package com.wbg.xigui.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.RecordBeanList

/**
 * @author tyk
 * @date :2019/9/5 14:48
 * @des :  亲情付 记录适配器
 */

class PayRecordAdapter : BaseQuickAdapter<RecordBeanList, BaseViewHolder>(R.layout.item_record_pay) {
    override fun convert(helper: BaseViewHolder?, item: RecordBeanList?) {
        item?.run {
            if (TextUtils.isEmpty(userName)){
                helper?.setText(R.id.tv_name, userNcikName)
            }else{
                helper?.setText(R.id.tv_name, "$userNcikName($userName)")
            }
            helper?.setText(R.id.tv_time, operationTime)
            helper?.setText(R.id.tv_money,"-¥$money" )
        }
    }
}