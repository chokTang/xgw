package com.wbg.xigui.viewmodel

import androidx.databinding.ViewDataBinding
import com.luck.picture.lib.entity.LocalMedia
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.databinding.AddPicItemBinding
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.PicSelectorUtil
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import otherwise
import yes

/**
 * @author xyx
 * @date :2019/6/20 16:33
 */
class PayStoreSuccessViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<LocalMedia>()
    val binding = ItemBinding.of<LocalMedia>(BR.bean, R.layout.add_pic_item)
    val adapter = (object : BindingRecyclerViewAdapter<LocalMedia>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: LocalMedia?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as AddPicItemBinding).run {
                item?.run {
                    path.isNullOrEmpty().yes {
                        img.setImageDrawable(ResourcesUtil.getDrawable(R.drawable.icon_pic_add))
                        img.setOnClickListener {
                            PicSelectorUtil.pick(
                                img.context,
                                4,
                                list.filter { it.path != null && it.path.isNotEmpty() })
                        }
                    }.otherwise {
                        GlideUtil.loadImg(path, img, img.context)
                    }
                }
            }
        }
    })

    fun getData() {
        list.replaceAll(arrayListOf(LocalMedia()))
    }

    fun refreshPic(imgs: List<LocalMedia>) {
        var templist = ArrayList<LocalMedia>()
        templist.addAll(imgs)
        if (templist.size < 4) {
            templist.add(LocalMedia())
        }
        list.replaceAll(templist)
    }
}