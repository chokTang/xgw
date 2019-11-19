package com.wbg.xigui.ui.mine

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.PwdLoginFragmentBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.PwdLoginViewModel
import kotlinx.android.synthetic.main.pwd_login_fragment.*
import startRout

/**
 * @author xyx
 * @date :2019/6/24 12:05
 * @des ：密码登录
 */
class PwdLoginFragment : BaseXFragment<PwdLoginViewModel>() {
    var binding: PwdLoginFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        login_btn.setOnClickListener {
            mViewModel.login {
            }
        }
        eyes_img.setOnClickListener {
            if (eyes_img.isSelected) {
                eyes_img.isSelected = false
                pwd_edt.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                eyes_img.isSelected = true
                pwd_edt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }
        forget_tv.setOnClickListener {
            startRout(RoutUrl.Common.set_pwd, SetPwdActivity.KEY_TYPE, 1)
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.pwd_login_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initImmersionBar() {

    }
}