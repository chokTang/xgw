package com.wbg.xigui.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.wbg.xigui.R

abstract class LoadViewHelper(private val mContext: Context) {
    private var layoutId = 0
    private var mTargetView: View? = null
    /**
     * @return 返回你替换进来的View
     */
    var view: View? = null
        private set

    /**
     * 用来替换某个View，比如你可以用一个空页面去替换某个View
     *
     * @param targetView 被替换的那个View
     * @return
     */

    fun showEmpty(text: String, targetView: View): LoadViewHelper {
        mTargetView = targetView
        if (mTargetView == null) {
            return this
        } else if (mTargetView!!.parent !is ViewGroup) {
            return this
        }

        val parentViewGroup = mTargetView!!.parent as ViewGroup
        val index = parentViewGroup.indexOfChild(mTargetView)
        if (view != null) {
            parentViewGroup.removeView(view)
        }
        if (layoutId == 0) {
            view = View.inflate(mContext, R.layout.empty_layout, null)
            view!!.layoutParams = mTargetView!!.layoutParams
            val ps = view!!.findViewById<TextView>(R.id.empty_ps)
            ps.text = text
            ps.setOnClickListener {
                action()
                ps.visibility = View.GONE
                view!!.findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
            }
        } else {
            view = View.inflate(mContext, layoutId, null)
            view!!.layoutParams = mTargetView!!.layoutParams
        }
        parentViewGroup.addView(view, index)

        //RelativeLayout时别的View可能会依赖这个View的位置，所以不能GONE
        if (parentViewGroup is RelativeLayout) {
            mTargetView!!.visibility = View.INVISIBLE
        } else {
            mTargetView!!.visibility = View.GONE
        }
        return this
    }

    /**
     * 移除你替换进来的View
     */
    fun showContent(): LoadViewHelper {
        if (view != null && mTargetView != null) {
            if (mTargetView!!.parent is ViewGroup) {
                val parentViewGroup = mTargetView!!.parent as ViewGroup
                parentViewGroup.removeView(view)
                view = null
                mTargetView!!.visibility = View.VISIBLE
            }
        }
        return this
    }

    fun showLoading(targetView: View): LoadViewHelper {
        mTargetView = targetView
        if (mTargetView == null) {
            return this
        } else if (mTargetView!!.parent !is ViewGroup) {
            return this
        }

        val parentViewGroup = mTargetView!!.parent as ViewGroup
        val index = parentViewGroup.indexOfChild(mTargetView)
        if (view != null) {
            parentViewGroup.removeView(view)
        }
        view = View.inflate(mContext, R.layout.empty_layout, null)
        view!!.layoutParams = mTargetView!!.layoutParams
        val ps = view!!.findViewById<TextView>(R.id.empty_ps)
        ps.visibility = View.GONE
        view!!.findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
        parentViewGroup.addView(view, index)

        //RelativeLayout时别的View可能会依赖这个View的位置，所以不能GONE
        if (parentViewGroup is RelativeLayout) {
            mTargetView!!.visibility = View.INVISIBLE
        } else {
            mTargetView!!.visibility = View.GONE
        }
        return this

    }

    fun setLayoutId(id: Int) {
        layoutId = id
    }

    abstract fun action()

}
