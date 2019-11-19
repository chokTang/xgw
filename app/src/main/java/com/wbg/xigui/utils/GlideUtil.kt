package com.wbg.xigui.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.wbg.xigui.R
import com.wbg.xigui.bean.BaseBean

class GlideUtil {
    companion object {
        private val headOption = RequestOptions().placeholder(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(R.mipmap.ic_launcher)
        private val normalOption = RequestOptions().placeholder(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.ic_launcher)

        fun loadHead(url: String, imageView: ImageView, context: Context) {
            Glide.with(context).load(url).apply(headOption).into(imageView)
        }

        fun loadImg(url: Any, imageView: ImageView, context: Context) {
            Glide.with(context).load(url).apply(normalOption).into(imageView)
        }
    }
}