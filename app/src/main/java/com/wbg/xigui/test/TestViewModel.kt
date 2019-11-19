package com.wbg.xigui.test

import android.os.Handler
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kw.rxbus.RxBus
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bus.RxEvent
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.getParam
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.widget.ObservableReplaceArrayList
import io.reactivex.disposables.Disposable
import log
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import startRout
import java.util.*
import kotlin.collections.HashMap


class TestViewModel : BaseViewModel() {
    private lateinit var disposable: Disposable
    override fun onCreate() {
        super.onCreate()
        disposable = RxBus.getInstance().toObservable(RxEvent::class.java).subscribe {
            log("====${it.msg}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister(disposable)
    }

    val list = ObservableReplaceArrayList<String>()
    val hello = ObservableField<String>()
    val binding = ItemBinding.of<String>(BR.item, R.layout.item_test)
    val adapter = (object : BindingRecyclerViewAdapter<String>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: String?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
            super.onBindViewHolder(holder, position, payloads)
        }
    })

    fun getData() {
        Handler().postDelayed({
            val data = arrayListOf("1", "2", "3", "1", "1", "2", "3", "1")
            hello.set("" + System.currentTimeMillis())
            val temp = Random().nextInt(10)
            if (temp % 3 == 0) {
                list.addAll(data)
                changeLoadType(list, data, false)
                errorMsg.value = "加载错误"
            } else
                changeLoadType(list, null, true)
        }, 1000)
    }

    fun refresh() {
        Handler().postDelayed(
            {
                val temp = Random().nextInt(10)
                val data = arrayListOf("1", "2", "3", "1", "1", "2", "3", "1")
                if (temp % 3 == 0) {
                    list.replaceAll(data)
                    changeLoadType(list, data, false)
                } else {
                    errorMsg.value = "刷新错误"
                    changeLoadType(list, null, true)
                }
            },
            1000
        )
    }

    fun startActivity() {
//        startRout(RoutUrl.Test.home_activity)
        var map = HashMap<String, Any>()
        map["test"] = 12
        map["th"] = 1256
        service.test(map.getParam()).netDispatch(object : RxNetObserver<Any>() {
            override fun onStart() {

            }

            override fun onError(msg: String) {
                log(msg)
            }

            override fun onSuccess(t: Any?) {
                log(t.toString())
            }

        })
    }
}
