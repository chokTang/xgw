package com.wbg.xigui.bind

import android.content.Context
import android.widget.ImageView
import com.wbg.xigui.utils.GlideUtil
import com.youth.banner.loader.ImageLoaderInterface

class GlideLoader : ImageLoaderInterface<ImageView> {
    override fun createImageView(context: Context?): ImageView {
        return ImageView(context)
    }

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideUtil.loadImg(path, imageView, context)
    }
}