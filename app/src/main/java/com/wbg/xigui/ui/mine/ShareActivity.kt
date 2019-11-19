package com.wbg.xigui.ui.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.utils.ZXingUtils
import com.wbg.xigui.viewmodel.ShareViewModel
import kotlinx.android.synthetic.main.activity_share.*

/**
 * @author tyk
 * @date :2019/9/6 16:54
 * @des : 分享页面
 */
@Route(path = RoutUrl.Mine.share)
class ShareActivity : BaseXActivity<ShareViewModel>() {

    var type:String? = "" //亲情付 传1  其他无所谓

    companion object{
        const val KEY_TYPE = "kety_type"//亲情付 传1  其他无所谓
    }

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        addView(R.layout.activity_share)
    }

    override fun initData() {
        type = intent.getIntExtra(KEY_TYPE,1).toString()
        mViewModel.getShareCode(type!!){
            val bitmap = ZXingUtils.createQRImage(it.code, ResourcesUtil.dip2px(165f),  ResourcesUtil.dip2px(165f))
            img_code.setImageBitmap(bitmap)
        }
    }
}