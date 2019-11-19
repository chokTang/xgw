package com.wbg.xigui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.wbg.xigui.R
import com.wbg.xigui.bean.OrderBean

/**
 * @author tyk
 * @date :2019/8/23 10:11
 * @des : 订单列表适配器   订单已经取消或者完成了的  再次购买改成删除
 */

class OrderAdapter : BaseQuickAdapter<OrderBean, BaseViewHolder>(R.layout.goods_order_item) {

    companion object {
        const val TYPE_PRODUCT_ORDER = 1//商品订单  是否是商家订单,1商品订单,2商家订单
        const val TYPE_MERCHANT_ORDER = 2//店铺订单
    }

    var adapter: MyOrderProductAdapter? = null

    init {
        multiTypeDelegate = object : MultiTypeDelegate<OrderBean>() {
            override fun getItemType(item: OrderBean?): Int {
                return if (item?.consigneeType == 2) {//店铺
                    TYPE_MERCHANT_ORDER
                } else {//商品
                    TYPE_PRODUCT_ORDER
                }
            }
        }

        multiTypeDelegate.registerItemType(TYPE_PRODUCT_ORDER, R.layout.goods_order_item)
            .registerItemType(TYPE_MERCHANT_ORDER, R.layout.store_order_item)
    }

    override fun convert(helper: BaseViewHolder?, item: OrderBean?) {
        when (helper?.itemViewType) {
            TYPE_PRODUCT_ORDER -> {//正常商品订单
                val linearLayoutManager = LinearLayoutManager(mContext)
                helper.getView<RecyclerView>(R.id.rv_product).layoutManager = linearLayoutManager
                adapter = MyOrderProductAdapter()
                helper.getView<RecyclerView>(R.id.rv_product).adapter = adapter
                item?.run {
                    helper.setText(R.id.tv_create_time, createTime)
                    helper.setText(R.id.tv_all_num, "共计${list.size}件商品  总计：")
                    helper.setText(R.id.tv_all_money, "￥$totalAmount")
                    adapter?.setNewData(list)
                    helper.getView<TextView>(R.id.tv_order_type).text = when (status) {
                        -3 -> "已退货"
                        -2 -> "已退款"
                        -1 -> "已取消"
                        0 -> "未支付"
                        1 -> "支付中"
                        2 -> "已支付"
                        3 -> "已发货"
                        4 -> "已完成"
                        5 -> "申请退款"
                        6 -> "申请退货"
                        else -> {
                            "已完成"
                        }
                    }
                    when (status) {//订单已经取消或者完成了的  再次购买改成删除
                        -3, -2 -> {
                            helper.getView<TextView>(R.id.left_btn).visibility = View.VISIBLE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.GONE
                            helper.setText(R.id.left_btn, "查询详情")
                        }
                        -1 -> {//已取消
                            helper.getView<TextView>(R.id.left_btn).visibility = View.GONE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.VISIBLE
                            helper.setText(R.id.right_btn, "删除")
                        }
                        0 -> {
                            helper.getView<TextView>(R.id.left_btn).visibility = View.VISIBLE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.VISIBLE
                            helper.setText(R.id.right_btn, "立即支付")
                            helper.setText(R.id.left_btn, "取消订单")
                        }
                        1 -> {//未支付和支付中的 订单状态 左边那按钮 换成取消订单
                            helper.getView<TextView>(R.id.left_btn).visibility = View.VISIBLE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.GONE
                            helper.setText(R.id.left_btn, "取消订单")
                        }
                        2 -> {//已付款 未发货  催单
                            helper.getView<TextView>(R.id.left_btn).visibility = View.GONE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.VISIBLE
                            helper.setText(R.id.right_btn, "提醒发货")
                        }
                        3 -> {//"已发货"
                            helper.getView<TextView>(R.id.left_btn).visibility = View.VISIBLE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.VISIBLE
                            helper.setText(R.id.right_btn, "确认收货")
                            helper.setText(R.id.left_btn, "查看物流")
                        }
                        4 -> {//已完成
                            helper.getView<TextView>(R.id.left_btn).visibility = View.VISIBLE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.VISIBLE
                            helper.setText(R.id.right_btn, "删除订单")
                            helper.setText(R.id.left_btn, "评价")
                        }
                        5, 6 -> {
                            helper.getView<TextView>(R.id.left_btn).visibility = View.GONE
                            helper.getView<TextView>(R.id.right_btn).visibility = View.VISIBLE
                            helper.setText(R.id.right_btn, "查询进度")
                        }
                    }
                    helper.addOnClickListener(R.id.left_btn)
                    helper.addOnClickListener(R.id.right_btn)
                }

            }

            TYPE_MERCHANT_ORDER -> {//商家订单
//                startRout(RoutUrl.Near.pay_store, "bean", bean)
                val linearLayoutManager = LinearLayoutManager(mContext)
                helper.getView<RecyclerView>(R.id.rv_product).layoutManager = linearLayoutManager
                adapter = MyOrderProductAdapter()
                helper.getView<RecyclerView>(R.id.rv_product).adapter = adapter
                item?.run {
                    helper.setText(R.id.tv_create_time, createTime)
                    helper.setText(R.id.tv_all_num, "共计${total}件商品  总计：")
                    helper.setText(R.id.tv_all_money, "￥$totalAmount")
                    adapter?.setNewData(list)
                    helper.getView<TextView>(R.id.tv_order_type).text = when (status) {
                        -3 -> "已退货"
                        -2 -> "已退款"
                        -1 -> "已取消"
                        0 -> "未支付"
                        1 -> "支付中"
                        2 -> "已支付"
                        3 -> "已发货"
                        4 -> "已完成"
                        5 -> "申请退款"
                        6 -> "申请退货"
                        else -> {
                            "已完成"
                        }
                    }
                }


            }
        }
    }


}