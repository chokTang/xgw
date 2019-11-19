package com.wbg.xigui.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import toast

import java.util.HashMap

/**
 * WXPayEntryActivity 微信支付回调类
 */
class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, WeChatPay.APP_ID)
        api!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        if (req is PayReq) {
            //获取携带相关参数
        }
    }

    override fun onResp(resp: BaseResp) {
        val bundle = Bundle()
        resp.toBundle(bundle)
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when {
                resp.errCode == 0 -> {
                    toast("支付成功")
                    finish()
                }
                resp.errCode == -1 -> {
                    toast("支付失败")
                    finish()
                }
                else -> {
                    toast("支付操作已取消")
                    finish()
                }
            }
        }
    }

    companion object {
        var orderInfor: Map<String, Any> = HashMap()
        var scenesId: String? = null
    }
}
