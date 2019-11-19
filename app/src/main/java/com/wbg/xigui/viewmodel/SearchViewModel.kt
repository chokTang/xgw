package com.wbg.xigui.viewmodel

import Preference
import android.app.AlertDialog
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.wbg.xigui.BR
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseViewModel
import com.wbg.xigui.bean.HistoryBean
import com.wbg.xigui.bean.SearchHotWordBean
import com.wbg.xigui.databinding.HistoryItemBinding
import com.wbg.xigui.net.RxNetObserver
import com.wbg.xigui.net.service
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.near.SearchResultActivity.Companion.KEY_WORD
import com.wbg.xigui.widget.ObservableReplaceArrayList
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import netDispatch
import no
import otherwise
import startRout
import yes

/**
 * @author xyx
 * @date :2019/6/20 14:59
 */
class SearchViewModel : BaseViewModel() {
    val hotList = MutableLiveData<List<SearchHotWordBean>>()
    val historyList = ObservableReplaceArrayList<HistoryBean>()
    val hisBinding = ItemBinding.of<HistoryBean>(BR.bean, R.layout.history_item)
    var list = ArrayList<HistoryBean>()
    val adapter = (object : BindingRecyclerViewAdapter<HistoryBean>() {
        override fun onBindBinding(
            binding: ViewDataBinding,
            variableId: Int,
            layoutRes: Int,
            position: Int,
            item: HistoryBean?
        ) {
            super.onBindBinding(binding, variableId, layoutRes, position, item)
            (binding as HistoryItemBinding).run {
                wordTv.setOnLongClickListener {
                    AlertDialog.Builder(wordTv.context)
                        .setTitle("提示")
                        .setMessage("确定要删除该条搜索历史？")
                        .setPositiveButton("删除") { dialog, which -> delete(position) }
                        .show()
                    true

                }
                wordTv.setOnClickListener {
                    wordTv.text.isNullOrEmpty().no {
                        searchWord(wordTv.text.toString())
                    }
                }
            }
        }
    })

    fun delete(position: Int) {
        list.removeAt(position)
        Preference.putList(XApplication.instance, list)
        historyList.replaceAll(list)
    }

    fun searchWord(str: String) {
        var tempList = ArrayList<HistoryBean>()
        list.filter { str != it.word }.run {
            isEmpty().yes { tempList = ArrayList() }
                .otherwise { tempList = (this as ArrayList<HistoryBean>) }
        }
        if (tempList.size > 200) tempList.removeAt(tempList.size - 1)
        tempList.add(0, HistoryBean(str))
        Preference.putList(XApplication.instance, tempList)
        startRout(RoutUrl.Near.search_result, KEY_WORD, str)
    }

    /**
     * 获取搜索热词
     */
    fun getHotWordData() {
        val map = HashMap<String, Any>()
        service.getHotWordList(map).netDispatch(object : RxNetObserver<List<SearchHotWordBean>>() {
            override fun onError(msg: String) {
                errorMsg.value = msg
            }

            override fun onStart() {
            }

            override fun onSuccess(t: List<SearchHotWordBean>?) {
                hotList.value = t
            }
        })

    }

    fun initHistory() {
        list = Preference.getList(XApplication.instance, HistoryBean::class.java) as ArrayList<HistoryBean>
        historyList.replaceAll(list)
    }

    fun clearHistory(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("提示")
            .setMessage("确定要清空搜索历史？")
            .setPositiveButton("清空") { dialog, which ->
                Preference.putList(context, ArrayList<HistoryBean>())
                historyList.clear()
            }
            .show()
    }
}