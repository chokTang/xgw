package com.wbg.xigui.bean

import android.os.Bundle
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.product.ProductDetailActivity
import startRout

/**
 * @author tyk
 * @date :2019/8/28 14:03
 * @des :
 */
data class CategoryProductBean(
    val aftersaleGuarantee: Int? = 0,
    val album: String? = "",
    val auditStatus: Int? = 0,
    val createTime: String? = "",
    val creator: String? = "",
    val description: String? = "",
    val editor: String? = "",
    val id: String? = "",
    val itemUpshelf: Int? = 0,
    val keyword: String? = "",
    val merchant: String? = "",
    val name: String? = "",
    val productCategory: String? = "",
    val rank: Int? = 0,
    val recommendation: Int? = 0,
    val reserve1: String? = "",
    val reserve2: String? = "",
    val reserve3: String? = "",
    val reserve4: String? = "",
    val reserve5: String? = "",
    val sale: Int? = 0,
    val score: String? = "",
    val status: Int? = 0,
    val subtitle: String? = "",
    val thumbnail: String? = "",
    val updateTime: String? = ""
): BaseBean(){
    fun goTodetail(){
        val bundle = Bundle()
        bundle.putString(ProductDetailActivity.KEY_PRODUCYID, id)
        bundle.putString(ProductDetailActivity.KEY_SKU_ID, "")
        bundle.putInt(ProductDetailActivity.KEY_POSITION, 0)
        startRout(RoutUrl.Product.home, ProductDetailActivity.KEY_BUNDLE, bundle)
    }
}