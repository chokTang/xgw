package com.wbg.xigui.ui.main

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.SearchActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.search_activity.*
import no

/**
 * @author xyx
 * @date :2019/6/20 15:42
 */
@Route(path = RoutUrl.Common.search)
class SearchActivity : BaseXActivity<SearchViewModel>() {
    var binding: SearchActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
//        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        hideTitle()
        binding = bindLayout(R.layout.search_activity)
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.getHotWordData()
            search_edt.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    v.text.isNullOrEmpty().no {
                        mViewModel.searchWord(v.text.toString())
                    }
                }
                false
            }
            mViewModel.hotList.observe(this@SearchActivity, Observer {
                flow_layout.removeAllViews()
                it.forEach { it1 ->
                    val layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(0, ResourcesUtil.dip2px(7f), ResourcesUtil.dip2px(7f), 0)
                    val view = layoutInflater.inflate(R.layout.search_hot_item, null)
                    val textView = view.findViewById<TextView>(R.id.word_tv)
                    textView.text = it1.name
                    view.setOnClickListener { mViewModel.searchWord(it1.name!!) }
                    flow_layout.addView(view, layoutParams)
                }
            })
            clear_img.setOnClickListener {
                mViewModel.clearHistory(this@SearchActivity)
            }
            search_tv.setOnClickListener {
                searchEdt.text.toString().isNullOrEmpty().no {
                    mViewModel.searchWord(searchEdt.text.toString())
                }
            }
            back.setOnClickListener { onBackPressed() }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.initHistory()
    }
}