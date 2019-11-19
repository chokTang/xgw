package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.StoreTypeBean
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/9/2 16:33
 * @des :  附近模块中的 种类（模块）适配器
 */
class TypeAdapter : BaseQuickAdapter<StoreTypeBean, BaseViewHolder>(R.layout.near_store_type_item) {
    override fun convert(helper: BaseViewHolder?, item: StoreTypeBean?) {
        GlideUtil.loadImg(item?.thumbnail!!,helper?.getView<ImageView>(R.id.img_type)!!,mContext)
        helper.setText(R.id.tv_name, item.name)
    }
}