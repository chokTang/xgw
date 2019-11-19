package com.wbg.xigui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.wbg.xigui.R
import com.wbg.xigui.bean.FileBody
import com.wbg.xigui.utils.GlideUtil
import log

/**
 * @author tyk
 * @date :2019/8/14 16:07
 * @des :  评论 图片适配器  60dp
 */

class PicAdapter : BaseQuickAdapter<FileBody, BaseViewHolder>(R.layout.item_pic) {

    companion object {
        const val ADD = 0//添加图标  显示添加图标
        const val NO_ADD = 1  //图片
        const val ADD_PIC = "add_pic"  //添加图片按钮
    }
    init {
        multiTypeDelegate = object : MultiTypeDelegate<FileBody>() {
            override fun getItemType(t: FileBody?): Int {
                var type = 1
                type = if (t!!.file == ADD_PIC) {
                    ADD
                } else {
                    NO_ADD
                }
                return type
            }
        }
        multiTypeDelegate.registerItemType(ADD, R.layout.item_add_pic)
            .registerItemType(NO_ADD, R.layout.item_pic)
    }
    override fun convert(helper: BaseViewHolder?, item: FileBody?) {
        val pic = helper?.getView<ImageView>(R.id.img)
        when (helper!!.itemViewType) {
            ADD -> {//添加图标
                log("当前是添加图标")
            }
            NO_ADD -> {//图片
                GlideUtil.loadImg(item?.file!!, pic!!, mContext)
            }

        }

    }


}