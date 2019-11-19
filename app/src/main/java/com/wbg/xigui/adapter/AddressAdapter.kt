package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.X

/**
 * @author tyk
 * @date :2019/8/16 10:45
 * @des : 地址列表适配器
 */

class AddressAdapter : BaseQuickAdapter<X, BaseViewHolder>(R.layout.item_address) {
    override fun convert(helper: BaseViewHolder?, item: X?) {
        item?.run {
            helper?.getView<ImageView>(R.id.img_select)?.isSelected = defaultStatus=="1"
            helper?.setText(R.id.tv_name,name)
            helper?.setText(R.id.tv_phone,phoneNumber)
            helper?.setText(R.id.tv_address,province+city+region+detailAddress)
        }
    }
}