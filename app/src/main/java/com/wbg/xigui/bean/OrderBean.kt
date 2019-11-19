package com.wbg.xigui.bean

/**
 * @author tyk
 * @date :2019/7/5 14:42
 */
data class OrderBean(
        var consigneeAddress: String? = "",//收货人详细地址
        var consigneeMobile: String = "",//收货人手机号码
        var consigneeName: String = "",//收货人姓名
        var consigneeType: Int = 0,//是否是商家订单,1商品订单,2商家订单
        var createTime: String = "",//是订单创建时间
        var orderId: String = "",//订单ID
        var payment: Int = 0,//支付方式 1支付宝 2 微信,3小程序
        var status: Int = 0,//订单状态,订单状态只有4个,-1已取消,0未支付,1支付中,2已支付，4已完成
        var totalAmount: Float = 0f,//订单总金额
        var total: Int = 0,//商品总数量
        var list: MutableList<IndentProductSkuView>//此订单的快照列表
) : BaseBean()


data class IndentProductSkuView(
        var amount: Float = 0f,//快照总金额
        var bondPortion: Float = 0f,//	sku/商家债权兑换比例
        var comName: String? = "",//	快递公司名称
        var image: String? = "",//	商品/商家缩略图
        var merchantId: String? = "",//	商家id
        var merchantName: String? = "",//	商家名称
        var mobile: String? = "",//	商家名称
        var name: String? = "",//	商品/商家名称
        var price: Float = 0f,//	销售价格
        var productId: String? = "",//商品id
        var propertyValue: String? = "",//sku属性值
        var skuId: String? = "",//skuId
        var status: Int = 0,//快照状态,-3已退货-2已退款,-1已取消,0未支付,1支付中,2已支付，3已发货，4已完成，5申请退款，6申请退货
        var total: Int = 0,//购买数量
        var trackeNum: String? = "",//快递单号
        var trackingNo: String = "",//物流id
        var updateTime: String = "",//其他需要显示的时间,status=-3则是退货时间,status=-2则是退款时间,…依次类推,status=0时请使用订单创建时间
        var list: MutableList<TrackingInfo>,//物流信息
        var state: Int = 0// "快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回 等7个状态"

) : BaseBean()

data class TrackingInfo(
        var areaCode: String = "",//本数据元对应的行政区域的编码
        var areaName: String = "",//本数据元对应的行政区域的名称
        var context: String = "",//物流轨迹节点内容
        var ftime: String = "",//格式化后时间
        var status: String = "",//本数据元对应的签收状态
        var time: String = ""//时间，原始格式
) : BaseBean()