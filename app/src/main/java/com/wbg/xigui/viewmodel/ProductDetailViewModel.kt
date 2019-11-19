package com.wbg.xigui.viewmodel

import android.text.TextUtils
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.*
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.utils.PamasUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import netDispatch

/**
 * @author tyk
 * @date :2019/8/12 10:48
 * @des : 商品详情model
 */
class ProductDetailViewModel : BaseViewModel() {
    val list = ObservableReplaceArrayList<CommentBean>()
    var id = ""
    /**
     * 获取商品详情
     */
    fun getProductDetail(
        productId: String,
        skuId: String,
        block: (bean: ProductDetailBean) -> Unit
    ) {
        val map = HashMap<String, Any>()
        map["productId"] = productId
        if (!TextUtils.isEmpty(skuId)) {
            map["skuId"] = skuId
        }
        service.getProductDetail(map).netDispatch(object : RxNetObserver<ProductDetailBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: ProductDetailBean?) {
                t?.run {
                    block(this)
                }

            }
        })
    }


    /**
     * 获取评论列表
     */
    fun getComment(block: (list: List<CommentBean>) -> Unit) {
        val map = HashMap<String, Any>()
        map["productId"] = id
        map["page"] = mPage
        map["size"] = 20
        service.getCommentList(map).netDispatch(object : RxNetObserver<List<CommentBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
                changeLoadType(list, null, false)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<CommentBean>?) {
                mPage++
                t?.run {
                    list.addAll(this)
                    block(list)
                }
                if (t == null) {
                    block(emptyList())
                }
            }
        })

    }

    /**
     * 刷新 初始化评论列表
     */
    fun refresh(block: (list: List<CommentBean>) -> Unit) {
        mPage = 1
        list.clear()
        getComment(block)
    }

    /**
     * 获取推荐商品列表
     */
    fun getProductRecList(
        productId: String,
        skuId: String,
        block: (list: List<ProductRecBean>) -> Unit
    ) {
        val map = HashMap<String, Any>()
        map["productId"] = productId
//        map["productId"] = "1004629641"
        if (!TextUtils.isEmpty(skuId)) {
            map["skuId"] = skuId
        }
        service.getProductRecList(map).netDispatch(object : RxNetObserver<List<ProductRecBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<ProductRecBean>?) {
                t?.run {
                    block(t)
                }
                if (t == null) {
                    block(emptyList())
                }
            }
        })

    }


    /**
     * 将商品加入购物车
     */
    fun addCar(addCarBody: AddCarBody, block: (addCar: FlagBean) -> Unit) {
        val map = PamasUtil.objectToMap(addCarBody) as HashMap<String, @JvmSuppressWildcards Any>
        service.addCar(map).netDispatch(object : RxNetObserver<FlagBean>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: FlagBean?) {
                t?.run {
                    block(t)
                }
            }
        })

    }


}