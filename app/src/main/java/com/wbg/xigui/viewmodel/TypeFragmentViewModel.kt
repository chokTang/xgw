package com.wbg.xigui.viewmodel

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.BannerBean
import com.wbg.xigui.bean.CategoryProductBean
import com.wbg.xigui.bean.ProductTypeBean
import com.wbg.xigui.databinding.TypeWordItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import otherwise
import toast
import yes

class TypeFragmentViewModel : BaseViewModel() {
    val typeList = ObservableReplaceArrayList<ProductTypeBean>()
    val goodsList = ObservableReplaceArrayList<CategoryProductBean>()
    val bannerList = ObservableReplaceArrayList<BannerBean>()
    val typeBinding = ItemBinding.of<ProductTypeBean>(BR.bean, R.layout.type_word_item)
    val goodsBinding = ItemBinding.of<CategoryProductBean>(BR.bean, R.layout.type_goods_item)
    var currentPosition = 0
    val typeAdapter = (object : BindingRecyclerViewAdapter<ProductTypeBean>() {
        override fun onBindBinding(
                binding: ViewDataBinding,
                variableId: Int,
                layoutRes: Int,
                position: Int,
                item: ProductTypeBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as TypeWordItemBinding).run {
                item?.run {
                    root.setOnClickListener {
                        getCateGoryProductList(id ?: "")
                        typeList[currentPosition].selected = false
                        typeList[position].selected = true
                        currentPosition = position
                        notifyDataSetChanged()
                    }
                    selected.yes {
                        typeTv.setBackgroundResource(R.drawable.type_word_corner_bg)
                        typeTv.setTextColor(Color.WHITE)
                    }
                            .otherwise {
                                typeTv.setBackgroundResource(R.color.transparent)
                                typeTv.setTextColor(ResourcesUtil.getColor(R.color.text_black))
                            }
                }

            }
        }
    })

    /**
     * 获取banner和 品类
     */
    fun getData() {
        getBanner()
        getCateGory()
    }



    /**
     * 获取banner  分类中的
     */
    fun getBanner() {
        val map = HashMap<String, Any>()
        map["location"] = 2
        service.getBanner(map.getParam()).netDispatch(object : RxNetObserver<List<BannerBean>>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<BannerBean>?) {
                t?.run {
                    bannerList.replaceAll(this)
                }

            }
        })
    }


    /**
     * 获取 商品品类列表
     */
    fun getCateGory() {
        val map = HashMap<String, Any>()
        service.getCategoryList(map).netDispatch(object : RxNetObserver<List<ProductTypeBean>>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(list: List<ProductTypeBean>?) {
                typeList.replaceAll(list)
                getCateGoryProductList(list!![0].id ?: "")
            }
        })
    }


    /**
     * 获取 商品品类下面的商品列表
     */
    fun getCateGoryProductList(id: String) {
        val map = HashMap<String, Any>()
        map["id"] = id
        service.getCategoryProductList(map).netDispatch(object : RxNetObserver<List<CategoryProductBean>>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<CategoryProductBean>?) {
                goodsList.replaceAll(t)

            }
        })
    }


    /**
     * 进入商品详情
     */
    fun goToProductDetail(){

    }


}