package com.wbg.xigui.ui.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.databinding.SetPwdActivityBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.SetPwdViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.code_login_fragment.send_code_btn
import kotlinx.android.synthetic.main.set_pwd_activity.*
import otherwise
import yes

/**
 * @author xyx
 * @date :2019/6/26 9:48
 */
@Route(path = RoutUrl.Common.set_pwd)
class SetPwdActivity : BaseXActivity<SetPwdViewModel>() {
    companion object{
        const val KEY_TYPE = "type"
    }
    var binding: SetPwdActivityBinding? = null
    var disposable: Disposable? = null
    var type = 1//1为忘记密码2为修改密码
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("")
        binding = bindLayout(R.layout.set_pwd_activity)
    }

    override fun initData() {
        binding?.run {
            model = mViewModel
            type = intent.getIntExtra(KEY_TYPE, 1)
            (type==1).yes { //忘记密码
                phone_ll.visibility=View.VISIBLE
                phone_ps.visibility=View.GONE
                top_title_tv.text="忘记密码"
            }.otherwise {
                phone_ll.visibility=View.GONE
                var phoneNum=XApplication.instance.getUserInfo().member?.userPhone?:""
                phone_ps.text= "${phoneNum.substring(0, 3)}****${phoneNum.substring(phoneNum.length - 3)}"
                phone_ps.visibility=View.VISIBLE
                top_title_tv.text="修改密码"
            }
            mViewModel.type = type
            sendCodeBtn.setOnClickListener {
                mViewModel.getCode {
                    timeAction()
                }
            }
            sure_btn.setOnClickListener {
                mViewModel.sure {
                    finish()
                }
            }
        }
    }

    private fun timeAction() {
        var time = 60
        //当获取验证码成功的时候倒计时开始
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

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}