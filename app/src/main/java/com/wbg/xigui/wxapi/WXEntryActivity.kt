/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.wbg.xigui.wxapi

import android.app.Activity
import android.os.Bundle
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import log
import toast


/**
 * 微信客户端回调activity示例
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, WeChatPay.APP_ID)
        api!!.handleIntent(intent, this)
    }

    /**
     * 处理微信发出的向第三方应用请求app message
     *
     *
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
    fun onGetMessageFromWXReq(msg: WXMediaMessage) {
        val iLaunchMyself = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(iLaunchMyself)
    }

    /**
     * 处理微信向第三方应用发起的消息
     *
     *
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     *
     *
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    fun onShowMessageFromWXReq(msg: WXMediaMessage?) {
        if (msg != null && msg.mediaObject != null
            && msg.mediaObject is WXAppExtendObject
        ) {
            val obj = msg.mediaObject as WXAppExtendObject
            toast( obj.extInfo)
        }
    }


    override fun onReq(baseReq: BaseReq) {}

    override fun onResp(baseResp: BaseResp) {
        if (baseResp.errCode == 0) {
            log("=====成功=====")
        } else {
            log("==========" + baseResp.errStr)
        }
        finish()
    }
}
