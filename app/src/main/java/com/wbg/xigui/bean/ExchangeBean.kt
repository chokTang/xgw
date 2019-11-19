package com.wbg.xigui.bean

/**
 * @author xyx
 * @date :2019/6/27 11:10
 */
data class ExchangeBean(
    val list: List<ExchangeBeanList?>? = listOf(),
    val retCode: String? = "",
    val retMsg: String? = ""
) : BaseBean()

data class ExchangeBeanList(
    val condition: List<String?>? = listOf(),
    val consumptionAmount: Float = 0f,
    val consumptionSingle:  Float = 0f,
    val consumptionsNumber: Int? = 0,
    val description: String? = "",
    val endTime: String? = "",
    val exchangeName: String? = "",
    val exchangeProportion:  Float = 0f,
    val exchangeProportionStr: String? = "",
    val id: String? = "",
    val maxBond:  Float = 0f,
    val minBond:  Float = 0f,
    val sureUser: String? = ""
) : BaseBean()