package com.wbg.xigui.net

import com.wbg.xigui.bean.*
import com.wbg.xlib.net.RetrofitClient
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


/**
 * <p>
 * 作者：jakee
 * 创建时间：2018/11/21
 */
interface Api {
    @POST("home/content2")
    fun test(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 登录
     */
    @POST("xgw/member/login")
    fun login(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<UserInfoBean>>

    /**
     * 选择角色
     */
    @POST("xgw/member/choseUserRole")
    fun chooseRole(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ChooseRoleBean>>

    /**
     * 发送验证码
     */
    @POST("xgw/member/sendCode")
    fun sendCode(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 债权人提交确权资料
     */
    @POST("xgw/member/addCreditInformation")
    fun postCreditorConfirm(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 债权人检测确权资料
     */
    @POST("xgw/member/checkCreditInformation")
    fun checkCreditorConfirm(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ConfirmSuccessBean>>

    /**
     * 债务人提交确权资料
     */
    @POST("xgw/member/addDebtInformation")
    fun postDebtorConfirm(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 获取代理商用户信息
     */
    @POST("xgw/member/getUserAccount")
    fun getUserInfo(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<AccountBean>>

    /**
     * 获取广告banner
     */
    @POST("advertisement/getadvertisement")
    fun getBanner(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<BannerBean>>>

    /**
     * 获取首页商品
     */
    @POST("homepageproduct/information")
    fun getMainGoods(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<MainGoodsBean>>

    /**
     * 修改密码
     */
    @POST("xgw/member/changePassword")
    fun setPwd(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 退出登录
     */
    @POST("xgw/member/logout")
    fun logout(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 文件上传
     */
    @Multipart
    @POST("xgw/member/upload")
    fun uploadFile(@Part list: List<MultipartBody.Part>): Observable<ReturnModel<UpLoadBean>>

    /**
     * 获取消息列表
     */
    @POST("xgw/member/getUserMessageInfo")
    fun getMsgList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ListBean<MsgBean>>>

    /**
     * 获取周边店铺列表
     */
    @POST("surrounding/merchant/index")
    fun getNearStore(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<StoreBean>>>

    /**
     * 获取周边店铺分类列表
     */
    @POST("surrounding/merchant/category")
    fun getStoreType(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<StoreTypeBean>>>

    /**
     * 获取店铺详情
     */
    @POST("surrounding/merchant/detail")
    fun getStoreDetail(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<StoreDetailBean>>

    /**
     * 获取评论列表
     */
    @POST("comment/list")
    fun getCommentList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<CommentBean>>>


    /**
     * 添加评论
     */
    @POST("comment/write")
    fun addComment(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>

    /**
     * 获取支付参数
     */
    @POST("merchant/order/create")
    fun getPayParam(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<PayBean>>


    /**
     * 重新发起支付
     */
    @POST("product/order/pay")
    fun rePay(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<PayOrderBean>>

    /**
     * 获取未读消息条数
     */
    @POST("xgw/member/getUnreadInformation")
    fun getUnreadMsgCount(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<MsgUnReadBean>>

    /**
     * 消息设置成已读
     */
    @POST("xgw/member/updateUserMessageInfo")
    fun setReaded(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 获取订单列表
     */
    @POST("order/list/query")
    fun getOrderList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<OrderBean>>>


    /**
     * 获取订单详情
     */
    @POST("order/list/detail")
    fun getOrderDetail(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<OrderBean>>


    /**
     * 我的退款/货申请列表
     */
    @POST("order/list/refundlist")
    fun getRefundList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<RefundBean>>>


    /**
     * 退款/退货详情
     */
    @POST("order/list/refunddetail")
    fun getRefundDetail(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<RefundBean>>

    /**
     * 申请退款/退货
     */
    @POST("order/list/refund")
    fun refund(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>

    /**
     * 删除订单
     */
    @POST("order/list/delete")
    fun deleteOrder(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>


    /**
     * 完成订单
     */
    @POST("order/list/complete")
    fun completeOrder(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>


    /**
     * 提醒发货
     */
    @POST("order/list/remind")
    fun remind(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>


    /**
     * 取消订单
     */
    @POST("order/list/cancel")
    fun cancelOrder(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>

    /**
     * 修改用户信息
     */
    @POST("xgw/member/updateUserInfo")
    fun updateUserInfo(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 添加银行卡信息
     */
    @POST("xgw/member/withdraw/bank/binding")
    fun bindBank(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ListBean<BankBean>>>

    /**
     * 获取银行卡列表
     */
    @POST("xgw/member/withdraw/bank/list")
    fun getBankList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ListBean<BankBean>>>

    /**
     * 提现
     */
    @POST("xgw/member/withdraw/withdraw")
    fun withdrawal(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 账户债权信息列表
     */
    @POST("xgw/member/account/getDebtorInfoList")
    fun getRightList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<AccountListBean>>

    /**
     * 设置优先级
     */
    @POST("xgw/member/account/priority")
    fun setPriority(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<AccountListBean>>

    /**
     * 获取账户余额信息
     */
    @POST("xgw/member/account/queryBalanceInfo")
    fun getBalance(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<AccountBalanceBean>>

    /**
     * 获取资金记录列表
     */
    @POST("xgw/member/account/queryDetail")
    fun getAccountRecord(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ListBean<MoneyRecordBean>>>

    /**
     * 获取商家订单消息列表
     */
    @POST("order/notice/query")
    fun getSupplierNews(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<SupplierNewsBean>>>

    /**
     * 获取商家债权比例
     */
    @POST("surrounding/merchant/bond")
    fun getStoreBond(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<StoreBean>>

    /**
     * 获取商家收益列表
     */
    @POST("xgw/member/account/queryIncome")
    fun getIncome(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ListBean<PaymentDetailBean>>>

    /**
     * 获取商家商品列表
     */
    @POST("productinfo/query")
    fun getSupplierGoods(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<SupplierGoodsBean>>>


    /**
     * 获取商品详情
     */
    @POST("product/detail")
    fun getProductDetail(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ProductDetailBean>>


    /**
     * 获取推荐商品列表
     */
    @POST("product/relevance")
    fun getProductRecList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<ProductRecBean>>>


    /**
     * 获取推荐商品列表
     */
    @POST("order/list/relevance")
    fun getOrderRecList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<ProductRecBean>>>


    /**
     * 获取地址列表
     */
    @POST("xgw/member/address/getMemberAddressList")
    fun getAddressList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<AddressBean>>


    /**
     * 添加新地址 新增地址
     */
    @POST("xgw/member/address/addMemberAddress")
    fun addAddress(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 修改地址
     */
    @POST("xgw/member/address/updateMemberAddress")
    fun updateAddress(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     * 删除地址
     */
    @POST("xgw/member/address/removeMemberAddress")
    fun deleteAddress(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>


    /**
     * 生成商品订单
     */
    @POST("product/order/create")
    fun createProductOrder(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<PayOrderBean>>


    /**
     * 将商品加入购物车
     */
    @POST("shoppingbag/add")
    fun addCar(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<FlagBean>>


    /**
     * 获取购物车中商品列表
     */
    @POST("shoppingbag/mybag")
    fun getCarProductList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<CartGoodsBean>>>


    /**
     * 获取购物车中推荐商品列表
     */
    @POST("shoppingbag/relevance")
    fun getCarProductRecList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<ProductRecBean>>>


    /**
     * 获取分类品类列表
     */
    @POST("category/list")
    fun getCategoryList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<ProductTypeBean>>>


    /**
     * 获取分类品类下面的商品列表
     */
    @POST("category/products")
    fun getCategoryProductList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<CategoryProductBean>>>


    /**
     * 获取搜索热词 随机获取5个搜索热词 无参
     */
    @POST("search/gethotWords")
    fun getHotWordList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<SearchHotWordBean>>>


    /**
     * 搜索商品
     */
    @POST("search/getproduct")
    fun searchProductList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<SearchProduct>>>


    /**
     * 搜索商家
     */
    @POST("search/getmerchant")
    fun searchMerchantList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<List<StoreBean>>>

    /**
     * 获取兑换列表
     */
    @POST("xgw/member/exchange/getExchangeInfo")
    fun getExchangeList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ExchangeBean>>


    /**
     *   判断用户是否有兑换条件
     */
    @POST("xgw/member/exchange/getExchangeConditions")
    fun checkIsCanExchange(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     *   判断用户是否有兑换条件
     */
    @POST("xgw/member/exchange/exchange")
    fun exchange(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     *   获取亲情付用户列表
     */
    @POST("xgw/member/family/getFamilyInfo")
    fun getFamilyList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<AvatarBean>>

    /**
     *   添加亲情号
     */
    @POST("xgw/member/family/addFamilyInfo")
    fun addFamilyList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     *   删除亲情号
     */
    @POST("xgw/member/family/delFamilyInfo")
    fun deleteFamilyList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>

    /**
     *   删除亲情号
     */
    @POST("xgw/member/family/getFamilyConsumption")
    fun getRecordList(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<RecordBean>>

    /**
     *   获取分享码
     */
    @POST("xgw/member/share/getShareCode")
    fun getShareCode(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<ShareCodeBean>>


    /**
     *   确认合同信息
     */
    @POST("xgw/member/account/sureContract")
    fun sureContract(@Body map: HashMap<String, @JvmSuppressWildcards Any>): Observable<ReturnModel<Any>>


}

val service: Api by lazy { RetrofitClient.client.create(Api::class.java) }
val servicePic: Api by lazy { RetrofitClient.clientPic.create(Api::class.java) }