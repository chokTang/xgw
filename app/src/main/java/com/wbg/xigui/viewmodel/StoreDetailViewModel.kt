package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.adapter.CommentPicAdapter
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.CommentBean
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.bean.StoreDetailBean
import com.wbg.xigui.databinding.CommentItemBinding
import com.wbg.xigui.databinding.StorePicItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.near.CommentListActivity.Companion.KEY_PRODUCT_ID
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import otherwise
import startRout
import yes

/**
 * @author xyx
 * @date :2019/6/19 15:09
 */
class StoreDetailViewModel : BaseViewModel() {
    val storePicList = ObservableReplaceArrayList<String>()
    val commentList = ObservableReplaceArrayList<CommentBean>()
    val detailBean = MutableLiveData<StoreDetailBean>()
    val storePicBinding = ItemBinding.of<String>(BR.url, R.layout.store_pic_item)
    val commentBinding = ItemBinding.of<CommentBean>(BR.bean, R.layout.comment_item)
    val picAdapter = (object : BindingRecyclerViewAdapter<String>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: String?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as StorePicItemBinding).run {
                img.setOnClickListener {
                    ARouter.getInstance().build(RoutUrl.Near.imageBrowser).withStringArrayList("data", storePicList)
                        .withInt("position", position).navigation()
                }
            }
        }
    })
    val commentAdapter = (object : BindingRecyclerViewAdapter<CommentBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: CommentBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as CommentItemBinding).run {
                item?.imgs.isNullOrEmpty().yes { pictureRecycler.visibility = View.GONE }.otherwise {
                    pictureRecycler.adapter = CommentPicAdapter(pictureRecycler.context, item?.imgs!!)
                    pictureRecycler.visibility = View.VISIBLE
                }
                item?.run {
                    ratingView.rating = score.toFloat()
                }
            }
        }
    })

    fun getData(bean: StoreBean) {
        getDetail(bean.id ?: "")
        getComment(bean.product ?: "")
    }

    fun goComment() {
        if (!detailBean.value?.product.isNullOrEmpty())
            startRout(RoutUrl.Near.comment_list, KEY_PRODUCT_ID, detailBean.value?.product ?: "")
    }

    fun getDetail(id: String) {
        val map = HashMap<String, Any>()
        map["id"] = id
        service.getStoreDetail(map).netDispatch(object : RxNetObserver<StoreDetailBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: StoreDetailBean?) {
                t?.run {
                    detailBean.value = this
                    storePicList.replaceAll(images)
                }
            }
        })
    }

    fun getComment(id: String) {
        val map = HashMap<String, Any>()
        map["productId"] = id
        map["page"] = 1
        map["size"] = 3
        service.getCommentList(map).netDispatch(object : RxNetObserver<List<CommentBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<CommentBean>?) {
                t?.run {
                    commentList.replaceAll(this)
                }
            }
        })
    }
}