package com.wbg.xigui.viewmodel

import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.FlagBean
import com.wbg.xigui.bean.body.RefundBody
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.utils.PamasUtil
import netDispatch

/**
 * @author xyx
 * @date :2019/7/10 10:07
 */
class RefundViewModel : BaseViewModel() {
//    val list = ObservableReplaceArrayList<LocalMedia>()
//    val binding = ItemBinding.of<LocalMedia> { itemBinding, position, item ->
//        if (item.path.isNullOrEmpty()) {
//            itemBinding.set(BR.bean, R.layout.refund_add_pic_item)
//        } else {
//            itemBinding.set(BR.bean, R.layout.add_pic_item)
//        }
//    }
//    val adapter = (object : BindingRecyclerViewAdapter<LocalMedia>() {
//        override fun onBindBinding(
//            binding: ViewDataBinding,
//            variableId: Int,
//            layoutRes: Int,
//            position: Int,
//            item: LocalMedia?
//        ) {
//            super.onBindBinding(binding, variableId, layoutRes, position, item)
//            item?.run {
//                path.isNullOrEmpty().yes {
//                    (binding as RefundAddPicItemBinding).run {
//                        root.setOnClickListener {
//                            PicSelectorUtil.pick(
//                                img.context,
//                                3,
//                                list.filter { it.path != null && it.path.isNotEmpty() })
//                        }
//                    }
//                }.otherwise {
//                    (binding as AddPicItemBinding).run {
//                        GlideUtil.loadImg(path, img, img.context)
//                    }
//                }
//            }
//        }
//    })
//
//    fun getData() {
//        list.replaceAll(arrayListOf(LocalMedia()))
//    }
//
//    fun refreshPic(imgs: List<LocalMedia>) {
//        var templist = ArrayList<LocalMedia>()
//        templist.addAll(imgs)
//        if (templist.size < 3) {
//            templist.add(LocalMedia())
//        }
//        list.replaceAll(templist)
//    }


    /**
     * 申请退货退款
     */
    fun refund(body: RefundBody?, block: (bean: FlagBean?) -> Unit) {
        service.refund(PamasUtil.objectToMap(body) as HashMap<String, @JvmSuppressWildcards Any>).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                block(t)
            }
        })
    }

}