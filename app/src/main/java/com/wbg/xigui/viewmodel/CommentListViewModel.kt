package com.wbg.xigui.viewmodel

import android.view.View
import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.adapter.CommentPicAdapter
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.CommentBean
import com.wbg.xigui.databinding.CommentItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import otherwise
import yes

/**
 * @author xyx
 * @date :2019/6/19 16:42
 */
class CommentListViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<CommentBean>()
    var id = ""
    val commentBinding = ItemBinding.of<CommentBean>(BR.bean, R.layout.comment_item)
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

            }
        }
    })

    fun getData() {
        val map = HashMap<String, Any>()
        map["productId"] = id
        map["page"] = mPage
        map["size"] = 20
        service.getCommentList(map).netDispatch(object : RxNetObserver<List<CommentBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
                changeLoadType(list, null, false)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<CommentBean>?) {
                mPage++
                t?.run {
                    list.addAll(this)
                }
                changeLoadType(list, t, false)
            }
        })

    }

    fun refresh() {
        mPage = 1
        list.clear()
        getData()
    }
}