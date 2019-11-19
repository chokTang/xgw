package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/29 15:19
 * @des : String 为数据源的适配器
 */
class PicStringAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_pic_80) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        GlideUtil.loadImg(item!!,helper?.getView<ImageView>(R.id.img_pic)!!,mContext)
    }
}