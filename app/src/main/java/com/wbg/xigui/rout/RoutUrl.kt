package com.wbg.xigui.rout

class RoutUrl {
    object Test {
        private const val test = "/test"
        const val test_activity = "$test/test"
        const val test_1 = "$test/test1"
    }

    object Near {
        private const val near = "/near"
        const val type_store = "$near/type_store"
        const val imageBrowser = "$near/image_browser"
        const val store_detail = "$near/store_detail"
        const val comment_list = "$near/comment_list"
        const val pay_store = "$near/pay_store"
        const val pay_store_success = "$near/pay_store_success"
        const val search_result = "$near/search_result"
    }

    object Main {
        private const val main = "/main"
        const val msg = "$main/msg"
        const val home_activity = "$main/home"
        const val msg_detail = "$main/msg_detail"

    }

    object Mine {
        private const val mine = "/mine"
        const val exchange = "$mine/exchange"
        const val person_info = "$mine/person_info"
        const val setting = "$mine/setting"
        const val order = "$mine/order"
        const val order_detail = "$mine/order_detail"
        const val refund = "$mine/refund"
        const val order_refund = "$mine/order_refund"
        const val refund_detail = "$mine/refund_detail"
        const val logistics_info = "$mine/logistics_info"
        const val account_info = "$mine/account_info"
        const val addressList = "$mine/addressList"
        const val add_address = "$mine/add_address"
        const val family_pay = "$mine/family_pay"
        const val about_us = "$mine/about_us"
        const val bankcard_manage = "$mine/bankcard_manage"
        const val share = "$mine/share"
        const val choice = "$mine/choice"
        const val store_join = "$mine/store_join"
        const val commit_data = "$mine/commit_data"
        const val commit_id_card_data = "$mine/commit_id_card_data"
        const val commit_business_data = "$mine/commit_business_data"
        const val commit_success = "$mine/commit_success"

    }

    object Common {
        private const val common = "/common"
        const val search = "$common/search"
        const val login = "$common/login"
        const val set_pwd = "$common/set_pwd"
        const val creditor_confirm = "$common/creditor_confirm"
        const val debtor_confirm = "$common/debtor_confirm"
        const val web_view = "$common/web_view"
        const val apply_withdrawal = "$common/apply_withdrawal"
    }

    object Supplier {
        private const val supplier = "/supplier"
        const val home = "$supplier/home"
        const val account = "$supplier/account"
        const val order_deliver = "$supplier/order_deliver"
        const val order_refund = "$supplier/order_refund"
        const val payment_detail = "$supplier/payment_detail"
    }

    object Product{
        private const val product = "/product"
        const val home = "$product/home"
    }

    object Order{
        private const val order = "/order"
        const val createOrder = "$order/createOrder"
        const val paySuccess = "$order/paySuccess"
    }

    object Comment{
        private const val comment = "/comment"
        const val addComment = "$comment/addComment"
    }


    object Agent {
        private const val agent = "/agent"
        const val home = "$agent/home"
        const val account = "$agent/account"
        const val join = "$agent/join"
    }

    object Debtor {
        private const val debtor = "/debtor"
        const val home = "$debtor/home"
    }

    object Extra {
        const val login = 10086
    }
}