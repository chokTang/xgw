package com.wbg.xigui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.wbg.xigui.R
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GlideUtil

/**
 * @author xyx
 * @date :2019/6/19 15:46
 */
class CommentPicAdapter(var context: Context, var imgs: ArrayList<String>) :
    RecyclerView.Adapter<CommentPicAdapter.PicHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.comment_pic_item, parent, false)
        return PicHolder(view)
    }

    override fun getItemCount() = imgs.size

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        holder.run {
            GlideUtil.loadImg(imgs[position], picImg, context)
            picImg.setOnClickListener {
                ARouter.getInstance().build(RoutUrl.Near.imageBrowser).withStringArrayList("data", imgs)
                    .withInt("position", position).navigation()
            }
        }
    }

    class PicHolder(view: View) : RecyclerView.ViewHolder(view) {
        val picImg: ImageView = view.findViewById(R.id.img)
    }
}