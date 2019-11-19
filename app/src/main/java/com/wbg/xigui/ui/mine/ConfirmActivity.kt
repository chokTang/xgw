package com.wbg.xigui.ui.mine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.ConfirmAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.ConfirmBean
import com.wbg.xigui.bean.Member
import com.wbg.xigui.bean.RoleType
import com.wbg.xigui.bean.UpLoadBean
import com.wbg.xigui.databinding.ConfirmActivityBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.*
import com.wbg.xigui.viewmodel.ConfirmViewModel
import com.wbg.xigui.widget.AndroidBug5497Workaround
import kotlinx.android.synthetic.main.confirm_activity.*
import log
import startRout
import toast

/**
 * @author xyx
 * @date :2019/6/26 14:53
 */
@Route(path = RoutUrl.Common.creditor_confirm)
class ConfirmActivity : BaseXActivity<ConfirmViewModel>() {
    var binding: ConfirmActivityBinding? = null
    var adapter: ConfirmAdapter? = null
    var position = 0
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("确权")
        setRightColor(ResourcesUtil.getColor(R.color.theme))
        setRightText("提交")
        setRightAction {
            log(adapter?.getResult().toString())
            if (creditor_name_edt.text.toString().isEmpty()) {
                toast("请输入债权人名字")
                return@setRightAction
            }
            if (!StrUtil.isIDCard(creditor_id_num_edt.text.toString())) {
                toast("请输入正确的债权人身份证号码")
                creditor_id_num_edt.error = "请输入正确的债权人身份证号码"
                return@setRightAction
            }
            mViewModel.checkInfo(
                creditor_name_edt.text.toString(),
                creditor_id_num_edt.text.toString(),
                adapter?.getResult() ?: ArrayList()
            ) { bean, map ->
                DialogUtil.showRightsInfo(this, bean) {
                    mViewModel.postInfo(map) {
                        var userInfoBean = XApplication.instance.getUserInfo()
                        userInfoBean.currentRole = RoleType.creditor
                        XApplication.instance.getMemberId()?.run {
                            userInfoBean.member = Member(id = this)
                            userInfoBean.token = XApplication.instance.getToken()
                        }
                        XApplication.instance.setUserInfo(userInfoBean)
                        startRout(RoutUrl.Main.home_activity)
                        XApplication.instance.clearActivity()
                    }
                }
            }
        }
        binding = bindLayout(R.layout.confirm_activity)

    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            adapter = ConfirmAdapter(this@ConfirmActivity, recyclerView, arrayListOf(ConfirmBean()))
            recyclerView.adapter = adapter
            add_info_btn.setOnClickListener { adapter?.addInfo() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidBug5497Workaround.assistActivity(this)
    }

    fun pickImg(position: Int) {
        this.position = position
        PicSelectorUtil.pick(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!= Activity.RESULT_OK){
            return
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            if (selectList.isNotEmpty()) {

                UploadUtil.uploadFile(1, selectList[0].path, object : RxNetObserver<UpLoadBean>(true, "正在上传图片……") {
                    override fun onError(msg: String) {
                        toast(msg)
                    }

                    override fun onStart() {

                    }

                    override fun onSuccess(t: UpLoadBean?) {
                        adapter?.setPic(selectList[0].path, position, t?.imgId ?: "")
                        log(t?.toString() ?: "")
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        XApplication.instance.setRole(null)
    }

}