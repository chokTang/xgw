package com.wbg.xigui.ui.near

import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.PopSortAdapter
import com.wbg.xigui.adapter.PopTypeAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.StoreTypeBean
import com.wbg.xigui.databinding.StoreForTypeActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.StoreForTypeViewModel
import com.wbg.xigui.widget.LoadViewHelper
import kotlinx.android.synthetic.main.store_for_type_activity.*

/**
 * @author xyx
 * @date :2019/6/18 17:42
 */
@Route(path = RoutUrl.Near.type_store)
class StoreForTypeActivity : BaseXActivity<StoreForTypeViewModel>() {
    var binding: StoreForTypeActivityBinding? = null
    var list: ArrayList<StoreTypeBean>? = null
    var currentTypeBean: StoreTypeBean? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return refresh_layout
    }

    override fun initView() {
        binding = bindLayout(R.layout.store_for_type_activity)
        setTitle("全部分类")
    }

    override fun initData() {
        val position = intent.getIntExtra("position", 0)
        list = intent.getSerializableExtra("list") as ArrayList<StoreTypeBean>
        mViewModel.currentType.observe(this@StoreForTypeActivity, Observer {
            mViewModel.refresh()
        })

        binding?.run {
            model = mViewModel
            loadViewHelper = (object : LoadViewHelper(this@StoreForTypeActivity) {
                override fun action() {
                    loadViewHelper?.showLoading(refreshLayout)
                    model?.refresh()
                }
            })
            list?.run {
                currentTypeBean = get(position)
                type_tv.text = currentTypeBean?.name
                mViewModel.currentType.value = currentTypeBean
            }
            refreshLayout.setOnLoadMoreListener { mViewModel.getData() }
            refreshLayout.setOnRefreshListener { mViewModel.refresh() }
            typeTv.setOnClickListener { showTypePop(typeTv.text.toString()) }
            sortTv.setOnClickListener { showSortPop() }
        }
    }

    private fun showTypePop(str: String) {
        var dra = ResourcesUtil.getDrawable(R.drawable.icon_up)
        dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
        type_tv.setTextColor(ResourcesUtil.getColor(R.color.theme))
        type_tv.setCompoundDrawables(null, null, dra, null)
        var view = layoutInflater.inflate(R.layout.pop_list, null)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var popWindow: PopupWindow? = null
        recyclerView.adapter = (object : PopTypeAdapter(currentTypeBean, this, list ?: ArrayList()) {
            override fun onItemClick(bean: StoreTypeBean) {
                type_tv.text = bean.name
                setTitle(bean.name)
                currentTypeBean = bean
                mViewModel.currentType.value = bean
                popWindow?.dismiss()
            }

        })
        popWindow =
            PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popWindow.setOnDismissListener {
            var dra = ResourcesUtil.getDrawable(R.drawable.icon_down)
            dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
            type_tv.setTextColor(ResourcesUtil.getColor(R.color.text_black))
            type_tv.setCompoundDrawables(null, null, dra, null)
        }
        popWindow.setBackgroundDrawable(BitmapDrawable())
        popWindow.isOutsideTouchable = true
        popWindow.showAsDropDown(top_ll, 0, 0)
    }

    private fun showSortPop() {
        var dra = ResourcesUtil.getDrawable(R.drawable.icon_up)
        dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
        sort_tv.setTextColor(ResourcesUtil.getColor(R.color.theme))
        sort_tv.setCompoundDrawables(null, null, dra, null)
        var view = layoutInflater.inflate(R.layout.pop_list, null)
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var list = arrayListOf("默认排序", "销量")
        var popWindow: PopupWindow? = null
        recyclerView.adapter = (object : PopSortAdapter(sort_tv.text.toString(), this, list) {
            override fun onItemClick(position: Int) {
                sort_tv.text = list[position]
                mViewModel.sort = position + 1
                mViewModel.refresh()
                popWindow?.dismiss()
            }

        })
        popWindow =
            PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popWindow.setOnDismissListener {
            var dra = ResourcesUtil.getDrawable(R.drawable.icon_down)
            dra.setBounds(0, 0, dra.minimumWidth, dra.minimumHeight)
            sort_tv.setTextColor(ResourcesUtil.getColor(R.color.text_black))
            sort_tv.setCompoundDrawables(null, null, dra, null)
        }
        popWindow.setBackgroundDrawable(BitmapDrawable())
        popWindow.isOutsideTouchable = true
        popWindow.showAsDropDown(top_ll, 0, 0)
    }
}