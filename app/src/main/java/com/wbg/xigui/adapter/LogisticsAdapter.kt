package com.wbg.xigui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.TrackingInfo

/**
 * @author tyk
 * @date :2019/8/28 9:28
 * @des : 物流信息 适配器
 */

class LogisticsAdapter : BaseQuickAdapter<TrackingInfo, BaseViewHolder>(R.layout.logistics_item) {
    override fun convert(helper: BaseViewHolder?, item: TrackingInfo?) {
        item?.run {
            if (helper?.adapterPosition == 0) {
                helper.getView<ImageView>(R.id.img_status)
                    .setImageResource(R.drawable.icon_logistics_point_red)
                helper.getView<TextView>(R.id.tv_status_des)
                    .setTextColor(mContext.resources.getColor(R.color.theme))
            } else {
                helper?.getView<ImageView>(R.id.img_status)
                    ?.setImageResource(R.drawable.icon_logistics_point_grey)
                helper?.getView<TextView>(R.id.tv_status_des)
                    ?.setTextColor(mContext.resources.getColor(R.color.text_80))
            }
            helper?.setText(R.id.tv_time, ftime)
            helper?.setText(R.id.tv_status_des, context)
        }
    }
}