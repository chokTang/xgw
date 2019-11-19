package com.wbg.xigui.ui.mine

import android.app.Activity
import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.PersonInfoActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.PicSelectorUtil
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.viewmodel.PersonInfoViewModel
import kotlinx.android.synthetic.main.person_info_activity.*
import startRout

/**
 * @author xyx
 * @date :2019/7/5 11:32
 */
@Route(path = RoutUrl.Mine.person_info, extras = RoutUrl.Extra.login)
class PersonInfoActivity : BaseXActivity<PersonInfoViewModel>() ,View.OnClickListener{


    var binding: PersonInfoActivityBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("个人资料")
        setRightColor(ResourcesUtil.getColor(R.color.theme))
        binding = bindLayout(R.layout.person_info_activity)
        setListener()
    }

    fun setListener(){
        ll_update_password.setOnClickListener(this)
    }


    override fun initData() {
        binding?.run {
            model = mViewModel
            mViewModel?.run {
                imgUrl.value = XApplication.instance.getUserInfo().member?.userIcon ?: ""
                nickName.value = XApplication.instance.getUserInfo().member?.userNcikName ?: ""
            }
        }
        head_ll.setOnClickListener { PicSelectorUtil.pick(this) }
        setRightText("保存")
        setRightAction {
            if (mViewModel.nickName.value.isNullOrEmpty()) {
                error_ps_tv.visibility = View.VISIBLE
                return@setRightAction
            }
            mViewModel.setInfo()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_update_password->{
                startRout(RoutUrl.Common.set_pwd, SetPwdActivity.KEY_TYPE, 2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode!= Activity.RESULT_OK){
            return
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            if (selectList.isNotEmpty()) {
                if (selectList[0].isCompressed){
                    mViewModel.uploadImg(selectList[0].compressPath)
                }else{
                    mViewModel.uploadImg(selectList[0].path)
                }
            }
        }
    }
}