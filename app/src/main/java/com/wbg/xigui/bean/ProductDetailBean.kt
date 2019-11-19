package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/8/13 10:06
 * @des : 商品详情页数据
 */

data class ProductDetailBean(
    //前面几个只是做的临时数据传送  不和服务器交互
    var num: Int? = 0,//用户订单时候的 购买个数
    var des: String? = "",//用于订单时候的描述
    var orderPrice:Float = 0f,//用于订单价格
    var bond: String? = "",// 用于订单 兑换比例

    val images: List<String?>? = listOf(),    //商品相册访问地址，第一张是主图
    val merchantId: String? = "",//商家id,为了方便立即购买功能
    val merchantName: String? = "",//商家名称,为了方便立即购买功能
    val mobile: String? = "",//商家客服号码
    val name: String? = "",// 商品名称
    val productId: String? = "",//商品ID
    val score: String? = "",//好评度
    val properties: List<Property?>? = listOf(),//此商品的属性列表,保证顺序，一定是0-在前面,然后才是1-,然后才是2-*.....
    var skuId: String? = "",// 如果不为空，那么这个字段就是你请求的时候给接口的skuId
    val skus: List<Sku?>? = listOf(),//此商品的sku列表
    val thumbnail: String? = ""//商品缩略图
) : BaseBean()

data class Property(
    val name: String? = "",//此属性的属性名
    val properties: List<PropertyX?>? = listOf() //此属性的属性值列表,保证顺序，一定是-0在前面,然后才是-1,然后才是*-2.....
) : BaseBean()

data class PropertyX(
    val arrayId: Int? = 0,
    val id: String? = "",//此属性的标识,格式0-0，第一个数字0表示的是属性名的编号，第二个数字表示的是属性值的编号
    val name: String? = "",//属性名
    val value: String? = "" //属性值

) : BaseBean()

data class Sku(
    val bond: Float? = 0.0f,
    val id: String? = "",
    val price: Float? = 0.0f,
    val propertiesId: List<String?>? = listOf(),
    val propertyValue: String? = "",
    val stock: Int? = 0
) : BaseBean()