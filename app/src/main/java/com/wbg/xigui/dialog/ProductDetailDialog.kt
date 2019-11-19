package com.wbg.xigui.dialog

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wbg.xigui.R
import com.wbg.xigui.adapter.ProductDialogAdapter
import com.wbg.xigui.bean.ProductDetailBean
import com.wbg.xigui.bean.Sku
import kotlinx.android.synthetic.main.dialog_select_product.*
import toast

/**
 * @author tyk
 * @date :2019/8/13 16:48
 * @des : 商品详情页  选择商品样式 dialog
 */

class ProductDetailDialog : BaseDialogFragment(), View.OnClickListener {


    var adapter: ProductDialogAdapter? = null
    var bean: ProductDetailBean? = null
    var allmoney = 0f
    var skuId = ""
    var num = 1
    var desStr = ""
    companion object {
        fun newIntance(): ProductDetailDialog {
            var dialog = ProductDetailDialog()
            return dialog
        }
    }

    override fun getResId(): Any {
        return R.layout.dialog_select_product
    }

    override fun initView() {

        allmoney = bean?.skus!![0]?.price!!
        skuId = bean?.skus!![0]?.id!!
        tv_money.text = "金额：¥$allmoney"
        tv_num.text = Editable.Factory.getInstance().newEditable(num.toString())

        val linearLayoutManager = LinearLayoutManager(context)
        rv_type.layoutManager = linearLayoutManager
        adapter = ProductDialogAdapter()
        rv_type.adapter = adapter

        setListener()
    }

    fun setListener() {
        ll_sub.setOnClickListener(this)
        ll_add.setOnClickListener(this)
        tv_add_car.setOnClickListener(this)
        tv_now_buy.setOnClickListener(this)
    }

    override fun initData() {
        adapter?.bean = bean
        adapter?.setNewData(bean!!.properties)

        adapter?.invoke(object : ProductDialogAdapter.ClickListener {
            @SuppressLint("SetTextI18n")
            override fun click(v: View?, sku: Sku?, listId: List<String>?, des: List<String>?) {
                allmoney = if (sku==null) {//没选完成的时候 显示默认第一个
                    bean?.skus!![0]?.price!!
                } else {
                    sku.price!!.toFloat()
                }

                skuId = if (sku==null) {//没选完成的时候 显示默认第一个
                    bean?.skus!![0]?.id!!
                } else {
                    sku.id!!
                }


                if (listId != null) {
                    var decString = ""
                    var sortId: MutableList<String> = arrayListOf()
                    //将listId 根据 0-1-2....排序拍出来
                    for (i in 0 until listId.size) {
                        for (j in 0 until listId.size) {
                            if (listId[j].startsWith(i.toString())) {
                                sortId.add(i,listId[j])
                            }
                        }
                    }

                    for (i in 0 until sortId.size) {//将描述用 ,拼接起来
                        if (sortId[i].startsWith(i.toString())) {
                            decString = if (i==0){
                                des!![i]
                            }else{
                                decString + "," + des!![i]
                            }
                        }
                    }
                    desStr= decString
                }

                tv_money.text = "金额：¥$allmoney"
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_sub -> {//减号
                if (num > 1) {
                    num--
                    tv_sub.setTextColor(context!!.resources.getColor(R.color.addColor))
                    tv_add.setTextColor(context!!.resources.getColor(R.color.subColor))
                    tv_num.text = Editable.Factory.getInstance().newEditable(num.toString())
                } else {
                    toast("最小值为1哟")
                }
            }
            R.id.ll_add -> {//加号
                num++
                tv_sub.setTextColor(context!!.resources.getColor(R.color.subColor))
                tv_add.setTextColor(context!!.resources.getColor(R.color.addColor))
                tv_num.text = Editable.Factory.getInstance().newEditable(num.toString())
            }
            R.id.tv_add_car -> {//加入购物车
                if (!TextUtils.isEmpty(desStr)){
                    clickListener?.click(v,desStr,num,allmoney*num,skuId)
                    dismiss()
                }else{
                    toast("请选择商品样式")
                }
            }
            R.id.tv_now_buy -> {//立即购买
                if (!TextUtils.isEmpty(desStr)){
                    clickListener?.click(v,desStr,num,allmoney*num,skuId)
                    dismiss()
                }else{
                    toast("请选择商品样式")
                }
            }
        }
    }

    /**
     * 初始化数据
     */
    fun setList(bean: ProductDetailBean): ProductDetailDialog {
        this.bean = bean
        return this
    }


    override fun getViewWidth(): Int {
        return -1
    }

    override fun getViewHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getViewGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun getAnimationType(): Int {
        return FORM_BOTTOM_TO_BOTTOM
    }

    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?): ProductDetailDialog {
        this.clickListener = clickListener
        return this
    }

    interface ClickListener {
        fun click(v: View?,des:String?,num:Int,money:Float?,skuId: String?)
    }

    var clickListener: ClickListener? = null
}