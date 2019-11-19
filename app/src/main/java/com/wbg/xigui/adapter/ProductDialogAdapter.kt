package com.wbg.xigui.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wbg.xigui.R
import com.wbg.xigui.bean.ProductDetailBean
import com.wbg.xigui.bean.Property
import com.wbg.xigui.bean.Sku
import com.wbg.xigui.utils.ListUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout


/**
 * @author tyk
 * @date :2019/8/13 16:22
 * @des : 商品选择种类适配器
 */
class ProductDialogAdapter : BaseQuickAdapter<Property, BaseViewHolder>(R.layout.item_product_dialog) {

    var list: MutableList<String> = arrayListOf()
    var listId: MutableList<String> = arrayListOf() //Property 中的ID用于 反查询sku中的 sku
    var listDes: MutableList<String> = arrayListOf() //选择东西的描述

    var selectNum = 0  //当 selectNum 选中数等于bean。property总数  证明每个选项都选择了的
    var bean: ProductDetailBean? = null

    override fun convert(helper: BaseViewHolder?, item: Property?) {
        val flowLayout = helper?.getView<TagFlowLayout>(R.id.flowlayout)
        helper?.setText(R.id.tv_type_1, item?.name)
        list.clear()
        for (i in 0 until item?.properties!!.size) {
            item.properties[i]!!.value?.let { list.add(it) }
        }
        //设置适配器
        flowLayout?.adapter = object : TagAdapter<String>(list) {
            override fun getView(parent: FlowLayout?, position: Int, str: String?): View {
                val tv = LayoutInflater.from(mContext).inflate(
                    R.layout.item_product_type,
                    flowLayout, false
                ) as TextView
                tv.text = str
                return tv
            }

            override fun onSelected(position: Int, view: View?) {
                view!!.isSelected = true
                selectNum++
                item.properties[position]!!.id?.let { listId.add(it) }
                item.properties[position]!!.value?.let { listDes.add(it) }
                if (selectNum == bean?.properties?.size) {//说明已经选择完成
                    for (i in 0 until bean?.skus!!.size) {
                        if (ListUtil.equals(listId as List<String?>?, bean?.skus!![i]!!.propertiesId)) {//对比sku中的值  相等就取出来
                            clickListener?.click(view, bean!!.skus!![i],listId,listDes)
                        }
                    }
                } else {//没选完成的时候 显示默认第一个
                    clickListener?.click(view, null,null,null)
                }
            }

            override fun unSelected(position: Int, view: View?) {
                view!!.isSelected = false
                if (listId.size > 0) {
                    item.properties[position]!!.id?.let { listId.remove(it) }
                    item.properties[position]!!.value?.let { listDes.remove(it) }
                }
                if (selectNum > 0) {
                    selectNum--
                }

                if (selectNum != bean?.properties?.size) {//没选完成的时候 显示默认第一个
                    clickListener?.click(view, null,null,null)
                }
            }
        }


    }


    /**
     * 接口
     */
    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun click(v: View?, sku: Sku?, listId: List<String>?, des: List<String>?)
    }

    var clickListener: ClickListener? = null
}