package com.wbg.xigui.viewmodel

import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.BannerBean
import com.wbg.xigui.bean.GoodsBean
import com.wbg.xigui.bean.MainGoodsBean
import com.wbg.xigui.bean.MsgUnReadBean
import com.wbg.xigui.bean.RoleType.Companion.agent
import com.wbg.xigui.bean.RoleType.Companion.creditor
import com.wbg.xigui.bean.RoleType.Companion.debtor
import com.wbg.xigui.databinding.HomeItemThreeBinding
import com.wbg.xigui.databinding.HomeItemTwoBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.product.ProductDetailActivity.Companion.KEY_BUNDLE
import com.wbg.xigui.ui.product.ProductDetailActivity.Companion.KEY_POSITION
import com.wbg.xigui.ui.product.ProductDetailActivity.Companion.KEY_PRODUCYID
import com.wbg.xigui.ui.product.ProductDetailActivity.Companion.KEY_SKU_ID
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import startRout
import toast


class HomeFragmentViewModel : BaseViewModel() {
    val jingList = ObservableReplaceArrayList<GoodsBean>()
    val playList = ObservableReplaceArrayList<GoodsBean>()
    val hotList = ObservableReplaceArrayList<GoodsBean>()
    val bannerList = ObservableReplaceArrayList<BannerBean>()
    val newsList = MutableLiveData<List<BannerBean>>()
    val newsNum = ObservableField<Int>()
    val jingBinding = ItemBinding.of<GoodsBean>(BR.bean, R.layout.home_item_two)
    val playBinding = ItemBinding.of<GoodsBean> { itemBinding, position, item ->
        if (position > 1) {
            itemBinding.set(BR.bean, R.layout.home_item_three)
        } else {
            itemBinding.set(BR.bean, R.layout.home_item_two)
        }
    }
    val hotBinding = ItemBinding.of<GoodsBean>(BR.bean, R.layout.home_item_three)
    val playLayoutManager: GridLayoutManager
        get() {
            val manager = GridLayoutManager(XApplication.instance, 6)
            manager.spanSizeLookup = (object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < 2) {
                        return 3
                    }
                    return 2
                }

            })
            return manager
        }
    val hotAdapter = (object : BindingRecyclerViewAdapter<GoodsBean>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
            super.onBindViewHolder(holder, position, payloads)
            val binding = DataBindingUtil.getBinding<HomeItemThreeBinding>(holder.itemView)
            binding?.run {
                val llparam = llRoot.layoutParams
                llparam.width = (ResourcesUtil.getScreenWidth() - ResourcesUtil.dip2px(60f)) / 3
                llRoot.layoutParams = llparam
            }
        }

        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: GoodsBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            binding.run {
                root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(KEY_PRODUCYID, item!!.id)
                    bundle.putString(KEY_SKU_ID, item.skuId)
                    bundle.putInt(KEY_POSITION, 0)
                    startRout(RoutUrl.Product.home, KEY_BUNDLE, bundle)
                }
            }

        }

    })
    val jingAdapter = (object : BindingRecyclerViewAdapter<GoodsBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: GoodsBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as HomeItemTwoBinding).run {
                oldPriceTv.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
            }
            binding.run {
                root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(KEY_PRODUCYID, item!!.id)
                    bundle.putString(KEY_SKU_ID, item.skuId)
                    bundle.putInt(KEY_POSITION, 0)
                    startRout(RoutUrl.Product.home, KEY_BUNDLE, bundle)
                }
            }
        }
    })
    val playAdapter = (object : BindingRecyclerViewAdapter<GoodsBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: GoodsBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            if (position < 2) {
                binding as HomeItemTwoBinding
                binding.run {
                    oldPriceTv.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
                }
            } else {
                binding as HomeItemThreeBinding
                binding.run {
                    oldPriceTv.paint.flags = STRIKE_THRU_TEXT_FLAG or ANTI_ALIAS_FLAG
                }
            }

            binding.run {
                root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(KEY_PRODUCYID, item!!.id)
                    bundle.putString(KEY_SKU_ID, item.skuId)
                    bundle.putInt(KEY_POSITION, 0)
                    startRout(RoutUrl.Product.home, KEY_BUNDLE, bundle)
                }
            }

        }
    })

    fun getData() {
        getBanner()
        getNews()
        getGoods()
    }

    fun goSearch() {
        startRout(RoutUrl.Common.search)
    }

    fun goMsg() {
        startRout(RoutUrl.Main.msg)
    }

    /**
     * 首页顶部banner
     */
    fun getBanner() {
        val map = HashMap<String, Any>()
        when(XApplication.instance.getRole()){
            creditor->{//债权人
                map["location"] = 1
            }
            debtor->{//债务人
                map["location"] = 5
            }
            agent->{//代理商
                map["location"] = 4
            }
            else ->{//默认债权人
                map["location"] = 1
            }
        }
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
     * 喜归快报
     */
    fun getNews() {
        val map = HashMap<String, Any>()
        map["location"] = 3
        service.getBanner(map.getParam()).netDispatch(object : RxNetObserver<List<BannerBean>>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {

            }

            override fun onSuccess(t: List<BannerBean>?) {
                t?.run {
                    newsList.value = this
                }

            }
        })
    }

    fun getGoods() {
        service.getMainGoods(HashMap()).netDispatch(object : RxNetObserver<MainGoodsBean>() {
            override fun onError(msg: String) {
                toast(msg)
            }

            override fun onStart() {
            }

            override fun onSuccess(t: MainGoodsBean?) {
                t?.run {
                    hotList.replaceAll(hot?.take(10))
                    playList.replaceAll(play?.take(5))
                    jingList.replaceAll(jing?.take(4))
                }
            }
        })
    }

    fun getUnReadNum() {
        val isLogin = !XApplication.instance.getUserInfo().token.isNullOrEmpty()
        if (!isLogin) {
            return
        }
        service.getUnreadMsgCount(HashMap()).netDispatch(object : RxNetObserver<MsgUnReadBean>() {
            override fun onError(msg: String) {
            }

            override fun onStart() {
            }

            override fun onSuccess(t: MsgUnReadBean?) {
                t?.run {
                    newsNum.set(unreadCount)
                }
            }
        })
    }
}