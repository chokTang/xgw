package com.wbg.xigui.ui.near

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.StoreBean
import com.wbg.xigui.bean.StoreDetailBean
import com.wbg.xigui.databinding.StoreDetailBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.MapUtil
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.viewmodel.StoreDetailViewModel
import kotlinx.android.synthetic.main.store_detail.*
import startRout

/**
 * @author xyx
 * @date :2019/6/19 16:22
 */
@Route(path = RoutUrl.Near.store_detail)
class StoreDetailActivity : BaseXActivity<StoreDetailViewModel>() {

    companion object{
        const val KEY_STORE_BEAN = "storebean"
    }

    var binding: StoreDetailBinding? = null
    var bean: StoreBean? = null


    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("店铺详情")
        bean = intent.getSerializableExtra(KEY_STORE_BEAN) as StoreBean?
        binding = bindLayout(R.layout.store_detail)
    }


    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel.detailBean.observe(this@StoreDetailActivity, Observer {
                bindData(it)
            })
            mViewModel.getData(bean!!)
            pay_btn.setOnClickListener {
                startRout(RoutUrl.Near.pay_store, "bean", bean!!)
            }
            address_tv.setOnClickListener {
                bean?.run {
                    MapUtil.goNav(
                        this@StoreDetailActivity,
                        latitude?.toDouble() ?: 0.0,
                        longitude?.toDouble() ?: 0.0,
                        addressTv.text.toString()
                    )
                }

            }
        }
    }

    private fun bindData(data: StoreDetailBean) {
        data.run {
            GlideUtil.loadImg(thumbnail ?: "", head_img, this@StoreDetailActivity)
            name_tv.text = name
            distance_tv.text = "${distance}m"
            rating_view.rating = score.toFloat()
            exchange_tv.text = "兑${StrUtil.subZeroAndDot(String.format("%.2f", (bond!! * 100)))}%"
            sales_tv.text = "销量${sale}份"
            time_tv.text = serviceTime
            address_tv.text = address
            call_ll.setOnClickListener {
                callPhone(mobile ?: "")
            }
        }
    }

    fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        startActivity(intent)
    }
}