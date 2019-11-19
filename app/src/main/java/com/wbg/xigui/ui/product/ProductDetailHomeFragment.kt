package com.wbg.xigui.ui.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kw.rxbus.RxBus
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.CommentAdapter
import com.wbg.xigui.adapter.GlideImageLoader
import com.wbg.xigui.adapter.ProductRecommendAdapter
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.bean.*
import com.wbg.xigui.bus.RxEvent
import com.wbg.xigui.dialog.ProductDetailDialog
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.order.CreateOrderActivity
import com.wbg.xigui.viewmodel.ProductDetailViewModel
import com.wbg.xigui.widget.DividerGridItemDecoration
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.item_layout_2.*
import kotlinx.android.synthetic.main.item_layout_3.*
import kotlinx.android.synthetic.main.item_layout_banner.*
import kotlinx.android.synthetic.main.item_layout_comment.*
import kotlinx.android.synthetic.main.item_layout_recommend_product.*
import kotlinx.android.synthetic.main.layout_bottom.*
import startRout
import toast

/**
 * @author tyk
 * @date :2019/8/12 15:02
 * @des : 产品详情页面(产品) 主页
 */

class ProductDetailHomeFragment : BaseXFragment<ProductDetailViewModel>(), View.OnClickListener {

    var productId: String? = null
    var skuid: String? = null
    var bean: ProductDetailBean? = null//实体类
    var number = 0 //数量
    var allmoney = 0f //金钱
    var commentAdapter: CommentAdapter? = null
    var recommendAdapter: ProductRecommendAdapter? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        setListener()
        mViewModel.getProductDetail(productId!!, skuid!!) { productBean ->
            bean = productBean
            //显示banner
            showBanner(productBean.images)
            showDes(productBean)
        }

        mViewModel.id = productId!!
        mViewModel.refresh {
            if (it.isEmpty()) {
                ll_comment.visibility = View.GONE
            } else {
                ll_comment.visibility = View.VISIBLE
                showCommentView(it)
            }
        }

        mViewModel.getProductRecList(productId!!, skuid!!) {
            if (it.isEmpty()) {
                ll_recommend_product.visibility = View.GONE
            } else {
                ll_recommend_product.visibility = View.VISIBLE
                showRecommendProduct(it)
            }
        }

        if (TextUtils.isEmpty(tv_selected_des.text.toString())) {//未选中样式
            ll_select.visibility = View.GONE
        } else {//选了样式
            ll_select.visibility = View.VISIBLE
        }


    }

    fun setListener() {
        ll_right_select.setOnClickListener(this)
        tv_all_comment.setOnClickListener(this)
        tv_custom_service.setOnClickListener(this)
        tv_goods.setOnClickListener(this)
        tv_add_car.setOnClickListener(this)
        tv_now_buy.setOnClickListener(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addView(inflater, R.layout.fragment_product, container)
        return mRootView
    }

    override fun initImmersionBar() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_right_select -> {//选择商品
                showDialog()
            }

            R.id.tv_all_comment -> {
                startRout(RoutUrl.Near.comment_list, "id", bean?.productId ?: "")
            }

            R.id.tv_custom_service -> {
                toast("你点击了服务按钮")
            }
            R.id.tv_goods -> {
                RxBus.getInstance().send(RxEvent("", 3))
                activity?.finish()
            }
            R.id.tv_add_car -> {
                showDialog()
            }
            R.id.tv_now_buy -> {

                if (TextUtils.isEmpty(tv_selected_des.text.toString())) {
                    showDialog()
                } else {
                    val bundle = Bundle()
                    bean?.num = number
                    bean?.des = tv_selected_des.text.toString()
                    bean?.orderPrice = allmoney
                    bean?.bond = tv_percent.text.toString()
                    bundle.putSerializable(CreateOrderActivity.KEY_PRODUCT_BEAN, bean)
                    startRout(RoutUrl.Order.createOrder, CreateOrderActivity.KEY_BUNDLE, bundle)
                }

            }
        }
    }

    fun showDialog() {
        ProductDetailDialog.newIntance().setList(bean!!)
                .invoke(object : ProductDetailDialog.ClickListener {
                    override fun click(
                            v: View?,
                            des: String?,
                            num: Int,
                            money: Float?,
                            skuId: String?
                    ) {
                        when (v?.id) {
                            R.id.tv_now_buy -> {
                                ll_select.visibility = View.VISIBLE
                                tv_selected_des.text = des
                                number = num
                                allmoney = money!!

                                val bundle = Bundle()
                                bean?.num = number
                                bean?.des = tv_selected_des.text.toString()
                                bean?.orderPrice = allmoney
                                bean?.bond = tv_percent.text.toString()
                                bean?.skuId = skuId
                                bundle.putSerializable(CreateOrderActivity.KEY_PRODUCT_BEAN, bean)
                                startRout(
                                        RoutUrl.Order.createOrder,
                                        CreateOrderActivity.KEY_BUNDLE,
                                        bundle
                                )
                            }
                            R.id.tv_add_car -> {
                                val body = AddCarBody()
                                body.memberId = XApplication.instance.getMemberId()
                                body.skuId = skuId
                                body.total = num
                                mViewModel.addCar(body) {
                                    if (!it.flag!!) {
                                        toast("当前库存不足")
                                    } else {
                                        toast("加入购物车成功")
                                    }
                                }
                            }
                        }
                    }
                }).show(fragmentManager, "product_select_dialog")
    }


    /**
     * 显示详情页banner
     */
    fun showBanner(list: List<String?>?) {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(GlideImageLoader())
        //设置图片集合
        banner.setImages(list)
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }

    /**
     * 显示商品描述
     */
    @SuppressLint("SetTextI18n")
    fun showDes(bean: ProductDetailBean?) {
        tv_name.text = bean?.name
        //价格兑换比例 显示
        if (TextUtils.isEmpty(bean?.skuId)) {//没有skuid
            val sku = bean?.skus?.get(0) as Sku
            tv_price.text = "¥${sku.price}"
            val bond = String.format("%.1f", sku.bond?.times(100))
            tv_percent.text = "兑换比例：$bond%"
        } else {//如果有 就要去匹配sku  数组
            for (i in 0 until bean?.skus!!.size) {
                val sku = bean.skus[i] as Sku
                val bond = String.format("%.1f", sku.bond?.times(100))
                val bond0 = String.format("%.1f", (bean.skus[0] as Sku).bond?.times(100))
                if (bean.skuId == sku.id) {
                    tv_price.text = "¥${sku.price}"
                    tv_percent.text = "兑换比例：$bond%"
                } else {
                    tv_price.text = "¥${(bean.skus[0] as Sku).price}"
                    tv_percent.text = "兑换比例：$bond0%"
                }
            }
        }
        //选中情况显示

    }

    /**
     * 显示评论模块
     */
    @SuppressLint("SetTextI18n")
    fun showCommentView(list: List<CommentBean>?) {
        if (list!!.isNotEmpty()) {
            ll_comment.visibility = View.VISIBLE
            tv_comment_num.text = "评论" + list[0].counts
        } else {
            ll_comment.visibility = View.GONE
        }
        tv_comment_percent.text = bean?.score

        val linearLayoutManager = LinearLayoutManager(activity)
        rv_comment.layoutManager = linearLayoutManager
        commentAdapter = CommentAdapter()
        rv_comment.adapter = commentAdapter
        if (list.size <= 3) {
            tv_all_comment.visibility = View.GONE
            commentAdapter?.setNewData(list)
        } else {
            tv_all_comment.visibility = View.VISIBLE
            commentAdapter?.setNewData(list.subList(0, 2))
        }

    }


    /**
     * 显示推荐商品模块
     */

    fun showRecommendProduct(list: List<ProductRecBean>?) {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        rv_recommend_product.layoutManager = gridLayoutManager
        recommendAdapter = ProductRecommendAdapter()
        rv_recommend_product.adapter = recommendAdapter
        rv_recommend_product.isNestedScrollingEnabled = false

        rv_recommend_product.addItemDecoration(
                DividerGridItemDecoration(
                        activity,
                        R.drawable.listdivider_white_10
                )
        )

        recommendAdapter?.setNewData(list)

        recommendAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.rl_add_car -> {
                    showDialog()

                }
            }
        }
    }
}