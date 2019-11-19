package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.adapter.LogisticsAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.comment.AddCommentActivity
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.viewmodel.LogisticsViewModel
import kotlinx.android.synthetic.main.logistics_info_activity.*

/**
 * @author xyx
 * @date :2019/7/15 16:44
 * @des  : 物流信息 订单跟踪
 */
@Route(path = RoutUrl.Mine.logistics_info, extras = RoutUrl.Extra.login)
class LogisticsInfoActivity : BaseXActivity<LogisticsViewModel>() {
    var orderBean: OrderBean? = null
    var adapter: LogisticsAdapter? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("订单跟踪")//物流信息
        addView(R.layout.logistics_info_activity)
        orderBean = intent.getBundleExtra(AddCommentActivity.KEY_BUNDLE).get(AddCommentActivity.KEY_OREDR_BEAN) as OrderBean

        val linearLayoutManager = LinearLayoutManager(this)
        rv_logistics.layoutManager = linearLayoutManager
        adapter = LogisticsAdapter()
        rv_logistics.adapter = adapter

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        mViewModel.getOrderDetail(orderBean?.orderId, orderBean!!.list[0].skuId) {
            adapter?.setNewData(it!!.list[0].list)

            GlideUtil.loadImg(it?.list!![0].image!!, img_product, this)
            when (it.list[0].state) {//"快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回 等7个状态"
                0 -> {
                    tv_status.text = "在途"
                }
                1 -> {
                    tv_status.text = "揽收"
                }
                2 -> {
                    tv_status.text = "疑难"
                }
                3 -> {
                    tv_status.text = "签收"
                }
                4 -> {
                    tv_status.text = "退签"
                }
                5 -> {
                    tv_status.text = "派件"
                }
                6 -> {
                    tv_status.text = "退回"
                }

            }
            tv_expressName.text = it.list[0].comName + ":" + it.list[0].trackeNum
            tv_phone.text = "客服电话:" + it.list[0].mobile
        }



    }
}