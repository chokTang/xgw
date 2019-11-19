package com.wbg.xigui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.components.ImmersionFragment
import com.gyf.immersionbar.components.SimpleImmersionFragment
import log

abstract class LazyFragment : ImmersionFragment() {
    /**
     * Fragment是否可见状态
     */
    var isFragmentVisible: Boolean = false
    /**
     * 标志位，View是否已经初始化完成。
     */
    private var isPrepared: Boolean = false
    /**
     * 是否第一次加载
     */
    private var isFirstLoad = true

    protected lateinit var mRootView: View

    /**
     * 初始化 View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = initView(inflater, container, savedInstanceState)

        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        isFirstLoad = true

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //界面初始化完成
        isPrepared = true
        loadData()
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            onVisible()
        } else {
            onInvisible()
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     * visible.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
            log("===可见===")
        } else {
            onInvisible()
            log("===不可见===")
        }
    }

     override fun onInvisible() {
         super.onInvisible()
        isFragmentVisible = false

    }

    /**
     * 当界面可见的时候执行
     */
    override fun onVisible() {
        super.onVisible()
        isFragmentVisible = true
        loadData()

    }

    /**
     * 这里执行懒加载的逻辑
     */
    protected open fun lazyLoad() {

    }

    private fun loadData() {
        //判断View是否已经初始化完成 并且 fragment是否可见 并且是第一次加载
        if (isPrepared && isFragmentVisible && isFirstLoad) {
            isFirstLoad = false
            lazyLoad()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isPrepared = false
    }
}
