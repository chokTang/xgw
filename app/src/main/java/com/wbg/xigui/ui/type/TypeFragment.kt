package com.wbg.xigui.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.TypeFragmentViewModel
import kotlinx.android.synthetic.main.type_fragment.*
import startRout

class TypeFragment : BaseXFragment<TypeFragmentViewModel>(),View.OnClickListener {


    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true, 0.2f).init()
    }

    var binding: com.wbg.xigui.databinding.TypeFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.type_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initData() {
        setListener()
        binding?.run {
            mViewModel.getData()
        }
    }

    fun setListener(){
        tv_search.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_search->{
                startRout(RoutUrl.Common.search)
            }
        }
    }
}