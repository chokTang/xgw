package com.wbg.xigui.ui.near

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.chrisbanes.photoview.PhotoView
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GlideUtil
import kotlinx.android.synthetic.main.img_browser_activity.*
import log

/**
 * @author xyx
 * @date :2019/6/19 16:05
 * 查看大图
 */
@Route(path = RoutUrl.Near.imageBrowser)
class ImgBrowserActivity : BaseXActivity<BaseViewModel>() {
    var banner_list: ArrayList<String> = ArrayList()
    private var points: Array<View?>? = null
    private var currentIndex: Int = 0
    private var isscrolling: Boolean = false
    private val adapter = BannerAdapter()
    var position = 0
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        ImmersionBar.with(this).reset().init()
        addView(R.layout.img_browser_activity)
        hideTitle()
    }

    override fun initData() {
        banner_list = intent.getStringArrayListExtra("data")
        position = intent.getIntExtra("position", 0)
        initBanner()
        common_vp.offscreenPageLimit = banner_list.size
        common_vp.adapter = adapter
        initPoint()
        if (position > 0 && position < banner_list.size) {
            common_vp.currentItem = position
        }
    }

    private fun initBanner() {
        common_vp.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setCurDot(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // TODO Auto-generated method stub
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // 未拖动页面时执行此处
                    isscrolling = false
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    // 正在拖动页面时执行此处
                    isscrolling = true
                }
            }
        })
    }

    private fun initPoint() {
        points = arrayOfNulls<View>(banner_list.size)
        ll_vp.gravity = Gravity.CENTER_VERTICAL
        ll_vp.removeAllViews()
        points?.run {
            // 循环取得小点图片
            for (i in 0 until (banner_list.size)) {
                // 得到一个LinearLayout下面的每一个子元素
                val ll = LayoutInflater.from(this@ImgBrowserActivity).inflate(
                    R.layout.point_img,
                    ll_vp, false
                )
                val imageView = ll.findViewById(R.id.img) as ImageView
                imageView.tag = i
                ll_vp.addView(ll)
                set(i, imageView)
                // 默认都设为灰色
                get(i)?.isEnabled = false
                get(i)?.setBackgroundResource(R.drawable.iv_guide_point_normal)
                // 给每个小点设置监听
                // points[i].setOnClickListener(this);
                // 设置位置tag，方便取出与当前位置对应
                get(i)?.tag = i
            }

            // 设置当面默认的位置
            currentIndex = 0
            // 设置为圆形，即选中状态
            get(currentIndex)?.setBackgroundResource(R.drawable.iv_guide_point_select)
        }
    }

    private fun setCurDot(positon: Int) {
        if (positon < 0 || positon > banner_list.size - 1
            || currentIndex == positon
        ) {
            return
        }
        points!![positon]?.setBackgroundResource(R.drawable.iv_guide_point_select)
        points!![currentIndex]?.setBackgroundResource(R.drawable.iv_guide_point_normal)
        currentIndex = positon
    }

    inner class BannerAdapter : PagerAdapter() {
        override fun finishUpdate(container: ViewGroup) {
            try {
                super.finishUpdate(container)
            } catch (nullPointerException: NullPointerException) {
                log("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate")
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(this@ImgBrowserActivity)
                .inflate(R.layout.hd_adapter_main_vp, null)
            val iv = view
                .findViewById(R.id.iv_adapter_main_vp) as PhotoView
            iv.setOnPhotoTapListener { _, _, _ -> onBackPressed() }
            GlideUtil.loadImg(banner_list!![position], iv, this@ImgBrowserActivity)
            container.addView(view)
            return view

        }

        override fun getCount(): Int {
            return banner_list?.size ?: 0
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

    }
}