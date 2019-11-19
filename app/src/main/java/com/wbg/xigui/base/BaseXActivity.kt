package com.wbg.xigui.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.BuildConfig
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.utils.LoadingType
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.common_layout.*
import toast
import yes
import java.lang.reflect.ParameterizedType


abstract class BaseXActivity<T : BaseViewModel> : AppCompatActivity() {
    private val tag by lazy {
        BaseXActivity::class.java.simpleName
    }
    lateinit var mViewModel: T
    var loadViewHelper: LoadViewHelper? = null
    var loadMore = true
    private lateinit var flContent: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        XApplication.instance.addActivity(this)
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG)
            Log.d(tag, this.javaClass.canonicalName)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.common_layout)
//        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true, 0.2f).init()
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.white)
            .statusBarDarkFont(true, 0.2f)
            .init()
        flContent = findViewById(R.id.fl_content)
        back_rl.setOnClickListener { onBackPressed() }
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val modelClass = (type).actualTypeArguments[0] as Class<T>
            mViewModel = ViewModelProviders.of(this).get(modelClass)
            mViewModel.run {
                loadType.observe(this@BaseXActivity, Observer {
                    onLoadChange(it)
                })
                errorMsg.observe(this@BaseXActivity, Observer {
                    toast(it)
                })
            }
        }
        initView()
        initData()
        mViewModel.onCreate()
    }

    fun onLoadChange(type: LoadingType) {
        val smartRefreshLayout = getSmartLayout()
        if (smartRefreshLayout != null) {
            when (type) {
                LoadingType.ERROR_EMPTY -> {
                    loadViewHelper?.showEmpty("网络错误\n点击屏幕重新加载", smartRefreshLayout)
                }
                LoadingType.ERROR_NOT_EMPTY -> {
                    smartRefreshLayout.finishLoadMore()
                    smartRefreshLayout.finishRefresh()
                }
                LoadingType.EMPTY -> {
                    loadViewHelper?.showEmpty("暂无数据\n点击屏幕刷新", smartRefreshLayout)
                }
                LoadingType.NO_MORE -> {
                    smartRefreshLayout.finishLoadMore()
                    smartRefreshLayout.isEnableLoadMore = false
                    toast("没有更多数据了哦")
                }
                LoadingType.SUCCESS -> {
                    loadViewHelper?.showContent()
                    loadMore.yes { smartRefreshLayout.isEnableLoadMore = true }
                    smartRefreshLayout.finishLoadMore()
                    smartRefreshLayout.finishRefresh()
                }
            }
        }
    }

    abstract fun getSmartLayout(): SmartRefreshLayout?
    /**
     * dataBinding模式添加layout
     *
     * @param layoutId
     * @param <D>
     * @return
    </D> */
    fun <D : ViewDataBinding> bindLayout(layoutId: Int): D? {
        return if (layoutId > 0) {
            flContent.removeAllViews()
            val a = DataBindingUtil.inflate<D>(layoutInflater, layoutId, flContent, true)
            if (a == null) {
                flContent.removeAllViews()
                layoutInflater.inflate(layoutId, flContent)
            } else {
                a.lifecycleOwner = this
            }
            a
        } else {
            null
        }
    }

    /**
     * 传统模式添加layout
     *
     * @param layoutId
     * @return
     */
    fun addView(layoutId: Int): View? {
        if (layoutId > 0) {
            flContent.removeAllViews()
            return layoutInflater.inflate(layoutId, flContent)
        } else {
            try {
                throw Exception("layout ID can not be 0")
            } catch (e: Exception) {
            }

            return null
        }
    }

    /**
     * 传统模式添加layout
     *
     * @param view
     * @return
     */
    fun addView(view: View?): View? {
        if (view != null) {
            flContent.removeAllViews()
            flContent.addView(view)
            return view
        } else {
            try {
                throw Exception("view can not be null")
            } catch (e: Exception) {

            }

            return null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.onDestroy()
        XApplication.instance.removeActivity(this)
    }

    override fun onResume() {
        XApplication.currentActivity = this
        super.onResume()
    }

    abstract fun initView()
    abstract fun initData()
    fun hideTitle() {
        title_fl.visibility = View.GONE
    }


    fun setTitleBackground(back: Int) {
        title_tv.setBackgroundColor(back)
        view_bg.setBackgroundColor(back)
    }

    fun setTitleTextColor(back: Int) {
        title_tv.setTextColor(back)
    }

    fun setRightTextColor(back: Int) {
        right_tv.setTextColor(back)

    }

    fun setTitle(title: String?) {
        title_tv.text = title ?: ""
    }

    fun setRightImg(id: Int) {
        var dra = ResourcesUtil.getDrawable(id)
        dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
        right_tv.setCompoundDrawables(null, null, dra, null)
    }

    fun setRightText(Str: String) {
        right_tv.text = Str
    }

    fun setRightColor(color: Int) {
        right_tv.setTextColor(color)
    }

    fun setRightAction(block: () -> Unit) {
        right_tv.setOnClickListener { block() }
    }
}