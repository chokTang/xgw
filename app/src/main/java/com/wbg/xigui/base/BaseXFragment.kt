package com.wbg.xigui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.utils.LoadingType
import com.wbg.xigui.widget.LoadViewHelper
import toast
import yes
import java.lang.reflect.ParameterizedType


abstract class BaseXFragment<T : BaseViewModel> : LazyFragment() {
    lateinit var mViewModel: T
    var loadViewHelper: LoadViewHelper? = null
    var loadMore = true//是否有分页
    fun <D : ViewDataBinding> bindLayout(inflater: LayoutInflater, layoutId: Int, container: ViewGroup?): D? {
        if (layoutId > 0) {
            var a: D? = null
            try {
                a = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
            } catch (e: Exception) {
                a = null
            }

            if (a == null) {
                mRootView = inflater.inflate(layoutId, container, false)
            } else {
                mRootView = a.root
                a.lifecycleOwner = this
            }
            return a
        } else {
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val modelClass = (type).actualTypeArguments[0] as Class<T>
            mViewModel = ViewModelProviders.of(this).get(modelClass)
            mViewModel.onCreate()
            mViewModel.run {
                loadType.observe(this@BaseXFragment, Observer {
                    onLoadChange(it)
                })
                errorMsg.observe(this@BaseXFragment, Observer {
                    context?.toast(it)
                })
            }
        }
    }

    fun addView(inflater: LayoutInflater, layoutId: Int, container: ViewGroup?) {
        mRootView = inflater.inflate(layoutId, container, false)
    }

    override fun lazyLoad() {
        super.lazyLoad()
        initData()
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
                    context?.toast("没有更多数据了哦")
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
    abstract fun initData()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.onDestroy()
    }
}