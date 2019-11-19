package com.wbg.xigui.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hard.imageratingview.ImageRatingView
import com.wbg.xigui.R
import com.wbg.xigui.bean.CommentBean
import com.wbg.xigui.bean.FileBody
import com.wbg.xigui.utils.GlideUtil

/**
 * @author tyk
 * @date :2019/8/14 15:13
 * @des : 评论适配器
 */

class CommentAdapter : BaseQuickAdapter<CommentBean, BaseViewHolder>(R.layout.item_comment) {
    var picAdapter:PicAdapter?=null
    var list:MutableList<FileBody> = arrayListOf()



    override fun convert(helper: BaseViewHolder?, item: CommentBean?) {
        var headView = helper!!.getView<ImageView>(R.id.img_header)
        val gridLayoutManager = GridLayoutManager(mContext,5)
        helper.getView<RecyclerView>(R.id.rv_pic).layoutManager = gridLayoutManager
        picAdapter = PicAdapter()
        helper.getView<RecyclerView>(R.id.rv_pic).adapter = picAdapter
        item?.run {
            GlideUtil.loadImg(head!!, headView, mContext)
            helper.setText(R.id.tv_name,userNickName)
            helper.setText(R.id.tv_content,comment)
            helper.setText(R.id.tv_product_des,name)
            helper.getView<ImageRatingView>(R.id.rating_view).rating = score.toFloat()
            for ( i in 0 until  images!!.size){
                val bean = FileBody(images[i],"",i)
                list.add(bean)
            }
            picAdapter?.setNewData(list)
        }
    }
}