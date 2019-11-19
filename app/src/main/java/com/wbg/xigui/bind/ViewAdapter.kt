package com.wbg.xigui.bind

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.wbg.xigui.R
import com.wbg.xigui.bean.BannerBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.common.WebViewActivity
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.ResourcesUtil
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import log
import otherwise
import q.rorbin.badgeview.QBadgeView
import startRout
import yes


@BindingAdapter(value = ["url", "head"], requireAll = false)
fun loadImage(imageView: ImageView, url: String?, head: Boolean) {
    head.yes {
        GlideUtil.loadHead(url ?: "", imageView, imageView.context)
    }
        .otherwise {
            GlideUtil.loadImg(url ?: "", imageView, imageView.context)
        }

}

@BindingAdapter(value = ["bannerUrls"], requireAll = false)
fun setImageFileOrUrl(banner: Banner, bannerUrls: List<BannerBean>?) {
    bannerUrls?.run {
        val urls = ArrayList<String>()
        bannerUrls.forEach {
            urls.add(it.img ?: "")
        }
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        banner.setImages(urls)
        banner.setImageLoader(GlideLoader())
        banner.isAutoPlay(true)
        banner.setDelayTime(5000)
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setOnBannerListener { bannerPosition ->
            log("${bannerUrls[bannerPosition].link}")
            startRout(RoutUrl.Common.web_view, WebViewActivity.KEY_LOAD_URL, bannerUrls[bannerPosition].link)
        }
        banner.start()
    }

}

@BindingAdapter(value = ["msgNum"], requireAll = false)
fun setMsgNum(view: View, msgNum: Int) {
    if (msgNum != 0) {
        QBadgeView(view.context)
            .setBadgePadding(1f, true)
            .setGravityOffset(0f, 0f, true)
            .setBadgeGravity(Gravity.END or Gravity.TOP)
            .setBadgeTextSize(10f, true)
            .bindTarget(view).setBadgeBackgroundColor(Color.WHITE).setBadgeTextColor(
                ResourcesUtil.getColor(
                    R.color.theme
                )
            ).badgeNumber = msgNum
    }
}
