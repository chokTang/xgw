package com.wbg.xigui.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.CodeLoginFragmentBinding
import com.wbg.xigui.push.TagAliasOperatorHelper
import com.wbg.xigui.utils.StrUtil
import com.wbg.xigui.viewmodel.CodeLoginViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.code_login_fragment.*
import toast


/**
 * @author xyx
 * @date :2019/6/24 12:04
 * @des ：验证码登录页面
 */
class CodeLoginFragment : BaseXFragment<CodeLoginViewModel>() {
    var binding: CodeLoginFragmentBinding? = null
    var disposable: Disposable? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        send_code_btn.setOnClickListener {
            if (!StrUtil.isMobileNo(mViewModel.phone.value)) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            var time = 60
            //当获取验证码成功的时候倒计时开始
            mViewModel.getCode {
                disposable = Observable.create(object : ObservableOnSubscribe<Int> {
                    override fun subscribe(emitter: ObservableEmitter<Int>) {
                        Thread(Runnable {
                            while (time >= 0) {
                                emitter.onNext(--time)
                                Thread.sleep(1000)
                            }
                        }).start()
                    }

                }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe {
                        if (it < 1) {
                            send_code_btn.isEnabled = true
                            send_code_btn.text = "发送验证码"
                        } else {
                            send_code_btn.isEnabled = false
                            send_code_btn.text = "重新获取${it}s"
                        }
                    }
            }
        }
        login_btn.setOnClickListener {
            val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
            tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET
            mViewModel.login {
            }
        }
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindLayout(inflater, R.layout.code_login_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    override fun initImmersionBar() {
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}