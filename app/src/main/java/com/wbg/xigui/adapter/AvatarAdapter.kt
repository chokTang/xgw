package com.wbg.xigui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.wbg.xigui.R
import com.wbg.xigui.bean.AvatarBeanList
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.widget.CircleImageView

/**
 * @author tyk
 * @date :2019/9/5 10:10
 * @des :
 */

class AvatarAdapter : BaseQuickAdapter<AvatarBeanList, BaseViewHolder>(R.layout.item_avatar_add) {

    companion object {
        const val TYPE_ADD = 0//添加头像图片
        const val TYPE_AVATAR = 1  //正常头像
    }

    init {
        multiTypeDelegate = object : MultiTypeDelegate<AvatarBeanList>() {
            override fun getItemType(t: AvatarBeanList?): Int {
                return t?.type!!
            }
        }


        multiTypeDelegate.registerItemType(TYPE_ADD, R.layout.item_avatar_add)
            .registerItemType(TYPE_AVATAR, R.layout.item_avatar)
    }

    override fun convert(helper: BaseViewHolder?, item: AvatarBeanList?) {
        when(helper?.itemViewType){
            TYPE_ADD->{//添加头像照片

            }
            TYPE_AVATAR->{//正常照片
                GlideUtil.loadHead(item?.userIcon!!, helper.getView<CircleImageView>(R.id.img_avatar),mContext)
                helper.setText(R.id.tv_name, item.userName)
            }
        }
    }
}