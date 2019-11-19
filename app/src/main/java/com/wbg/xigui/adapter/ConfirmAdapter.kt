package com.wbg.xigui.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.wbg.xigui.BuildConfig
import com.wbg.xigui.R
import com.wbg.xigui.bean.ConfirmBean
import com.wbg.xigui.ui.mine.ConfirmActivity
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.StrUtil
import kotlinx.android.synthetic.main.confirm_item.view.*
import no
import otherwise
import toast
import yes
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author xyx
 * @date :2019/6/26 15:29
 */
class ConfirmAdapter : RecyclerView.Adapter<ConfirmAdapter.ViewHolder> {
    private val tag by lazy {
        ConfirmAdapter::class.java.simpleName
    }
    var list: ArrayList<ConfirmBean>
    var context: Context
    var recyclerView: RecyclerView

    constructor(context: Context, recyclerView: RecyclerView, list: ArrayList<ConfirmBean>) {
        this.list = list
        this.context = context
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.confirm_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.d(tag,"onBindViewHolder position=$position")
        holder.run {
            list[position].run {
                debtorNameEdt.setText(name)
                debtorIdNumEdt.setText(idCard)
                debtorPhoneEdt.setText(phone)
                fileNumedt.setText(docId)
                courtNameEdt.setText(court)
                dateTv.text = judgmentTime
                rateEdt.setText(rate.toString())
                if (position == 0) {
                    deleteTv.visibility = View.GONE
                } else {
                    deleteTv.visibility = View.VISIBLE
                }
                deleteTv.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确定要删除该条债权人信息？")
                        .setPositiveButton("删除") { dialog, which ->
                            getListData(false)
                            list.removeAt(position)
                            recyclerView.isFocusableInTouchMode = false
                            notifyDataSetChanged()
                        }
                        .show()

                }
                rateEdt.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        var num = s.toString().isEmpty().no { s.toString().toDouble() }.otherwise { 0.0 }
                        if (num > 24) {
                            s?.run {
                                rateEdt.setText("24")
                                toast("最大不能超过24%")
                            }
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
                GlideUtil.loadImg(img, fileImg, context)
                hasFile.yes {
                    hasFileLl.visibility = View.VISIBLE
                    noFileLl.visibility = View.GONE
                    hasFileAmountEdt.setText("${(hasFileAmount == 0.0).yes { "" }.otherwise { hasFileAmount }}")
                }.otherwise {
                    hasFileLl.visibility = View.GONE
                    noFileLl.visibility = View.VISIBLE
                    noFileAmountEdt.setText("${(noFileAmount == 0.0).yes { "" }.otherwise { noFileAmount }}")
                }
                hasTv.isSelected = hasFile
                noTv.isSelected = !hasFile
                hasTv.setOnClickListener {
                    hasTv.isSelected = true
                    noTv.isSelected = false
                    hasFile = true
                    hasFileLl.visibility = View.VISIBLE
                    noFileLl.visibility = View.GONE
                }
                noTv.setOnClickListener {
                    hasTv.isSelected = false
                    noTv.isSelected = true
                    hasFile = false
                    hasFileLl.visibility = View.GONE
                    noFileLl.visibility = View.VISIBLE
                }
                dateTv.setOnClickListener {
                    val calender = Calendar.getInstance()
                    val year = calender.get(Calendar.YEAR)
                    val month = calender.get(Calendar.MONTH)
                    val day = calender.get(Calendar.DAY_OF_MONTH)
                    val pickDialog = DatePickerDialog(context, object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                            dateTv.text =
                                "$year-${((month + 1) < 10).yes { "0" + (month + 1) }.otherwise { month + 1 }}-${(dayOfMonth < 10).yes { "0$dayOfMonth" }.otherwise { dayOfMonth }}"
                        }

                    }, year, month, day)
                    pickDialog.show()
                }
                fileImg.setOnClickListener {
                    getListData(false)
                    (context as ConfirmActivity).pickImg(position)
                }

            }
        }
    }


    /*private val simpleTextWatcher by lazy {
        object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(this@ConfirmAdapter.tag,"$s")
            }

        }
    }*/

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var debtorNameEdt = view.findViewById<EditText>(R.id.debtor_name_edt)/*.apply {
            if(BuildConfig.DEBUG)
            addTextChangedListener(simpleTextWatcher)
        }*/
        var debtorIdNumEdt = view.findViewById<EditText>(R.id.debtor_id_num_edt)
        var debtorPhoneEdt = view.findViewById<EditText>(R.id.debtor_phone_edt)
        var hasTv = view.findViewById<TextView>(R.id.has_file)
        var noTv = view.findViewById<TextView>(R.id.no_file)
        var hasFileLl = view.findViewById<LinearLayout>(R.id.has_file_ll)
        var noFileLl = view.findViewById<LinearLayout>(R.id.no_file_ll)
        var fileNumedt = view.findViewById<EditText>(R.id.file_num_edt)
        var courtNameEdt = view.findViewById<EditText>(R.id.court_edt)
        var dateTv = view.findViewById<TextView>(R.id.date_tv)
        var rateEdt = view.findViewById<EditText>(R.id.rate_edt)
        var hasFileAmountEdt = view.findViewById<EditText>(R.id.has_file_amount_edt)
        var noFileAmountEdt = view.findViewById<EditText>(R.id.no_file_amount_edt)
        var fileImg = view.findViewById<ImageView>(R.id.file_img)
        var deleteTv = view.findViewById<TextView>(R.id.delete_tv)
    }

    fun getListData(isadd: Boolean): ArrayList<ConfirmBean> {
        var manager = recyclerView.layoutManager!!
        for (i in 0 until (manager.childCount)) {
            var view = manager.getChildAt(i)!!
            var holder = recyclerView.getChildViewHolder(view) as ViewHolder
            holder.run {
                list[i].run {
                    hasFile = hasFileLl.visibility == View.VISIBLE
                    rate =
                        rateEdt.text.toString().isEmpty().yes { 0.0 }.otherwise { rateEdt.text.toString().toDouble() }
                    court = courtNameEdt.text.toString()
                    docId = fileNumedt.text.toString()
                    name = debtorNameEdt.text.toString()
                    idCard = debtorIdNumEdt.text.toString()
                    phone = debtorPhoneEdt.text.toString()
                    hasFileAmount = hasFileAmountEdt.text.toString().isEmpty().yes { 0.0 }
                        .otherwise {
                            try {
                                hasFileAmountEdt.text.toString().toDouble()
                            } catch (e: Exception) {
                                0.0
                            }
                        }
                    noFileAmount = noFileAmountEdt.text.toString().isEmpty().yes { 0.0 }
                        .otherwise {
                            try {
                                noFileAmountEdt.text.toString().toDouble()
                            } catch (e: Exception) {
                                0.0
                            }
                        }
                    judgmentTime = dateTv.text.toString()
                    if (isadd) {
                        if(BuildConfig.DEBUG)
                        Log.d(this@ConfirmAdapter.tag,"$debtorNameEdt ${holder.itemView.debtor_name_edt}")
                        if (debtorNameEdt.text.toString().isEmpty()) {
                            toast("请输入债务人姓名")
                            return ArrayList()
                        }
                        if (!StrUtil.isIDCard(debtorIdNumEdt.text.toString())) {
                            toast("请输入正确的债务人身份证号码")
                            debtorIdNumEdt.error = "请输入正确的债务人身份证号码"
                            return ArrayList()
                        }
                        if (!StrUtil.isMobileNo(debtorPhoneEdt.text.toString())) {
                            toast("请输入正确的债务人手机号码")
                            debtorPhoneEdt.error = "请输入正确的债务人手机号码"
                            return ArrayList()
                        }
                        if (hasFile) {
                            if (fileNumedt.text.toString().isEmpty()) {
                                toast("请输入文书编号")
                                return ArrayList()
                            }
                            if (courtNameEdt.text.toString().isEmpty()) {
                                toast("请输入判决法院")
                                return ArrayList()
                            }
                            if (hasFileAmountEdt.text.toString().isEmpty() || hasFileAmountEdt.text.toString() == ".") {
                                toast("请输入金额")
                                return ArrayList()
                            }
                            if (dateTv.text.toString().isEmpty()) {
                                toast("请选择判决日期")
                                return ArrayList()
                            }
                            if (rateEdt.text.toString().isEmpty()) {
                                toast("请输入利率")
                                return ArrayList()
                            }
                        } else {
                            if (noFileAmountEdt.text.toString().isEmpty() || noFileAmountEdt.text.toString() == ".") {
                                toast("请输入金额")
                                return ArrayList()
                            }
                            if (img.isEmpty()) {
                                toast("请上传相关图片")
                                return ArrayList()
                            }
                        }
                    }
                }

            }
        }
        return list
    }

    fun addInfo() {
        getListData(true).isEmpty().yes {
            return
        }.otherwise {
            list.add(ConfirmBean())
            recyclerView.isFocusableInTouchMode = false
            notifyDataSetChanged()
        }
    }

    fun getResult(): ArrayList<ConfirmBean> {
        var manager = recyclerView.layoutManager!!
        for (i in 0 until (manager.childCount)) {
            var view = manager.getChildAt(i)!!
            var holder = recyclerView.getChildViewHolder(view) as ViewHolder
            holder.run {
                list[i].run {
                    hasFile = hasFileLl.visibility == View.VISIBLE
                    if (debtorNameEdt.text.toString().isEmpty()) {
                        toast("请输入债务人姓名")
                        return ArrayList()
                    }
                    if (!StrUtil.isIDCard(debtorIdNumEdt.text.toString())) {
                        toast("请输入正确的债务人身份证号码")
                        debtorIdNumEdt.error = "请输入正确的债务人身份证号码"
                        return ArrayList()
                    }
                    if (!StrUtil.isMobileNo(debtorPhoneEdt.text.toString())) {
                        toast("请输入正确的债务人手机号码")
                        debtorPhoneEdt.error = "请输入正确的债务人手机号码"
                        return ArrayList()
                    }
                    if (hasFile) {
                        if (fileNumedt.text.toString().isEmpty()) {
                            toast("请输入文书编号")
                            return ArrayList()
                        }
                        if (courtNameEdt.text.toString().isEmpty()) {
                            toast("请输入判决法院")
                            return ArrayList()
                        }
                        if (hasFileAmountEdt.text.toString().isEmpty()) {
                            toast("请输入金额")
                            return ArrayList()
                        }
                        if (dateTv.text.toString().isEmpty()) {
                            toast("请选择判决日期")
                            return ArrayList()
                        }
                        if (rateEdt.text.toString().isEmpty()) {
                            toast("请输入利率")
                            return ArrayList()
                        }
                    } else {
                        if (noFileAmountEdt.text.toString().isEmpty()) {
                            toast("请输入金额")
                            return ArrayList()
                        }
                        if (img.isEmpty()) {
                            toast("请上传相关图片")
                            return ArrayList()
                        }
                    }
                    rate =
                        rateEdt.text.toString().isEmpty().yes { 0.0 }.otherwise { rateEdt.text.toString().toDouble() }

                    docId = fileNumedt.text.toString()
                    name = debtorNameEdt.text.toString()
                    idCard = debtorIdNumEdt.text.toString()
                    phone = debtorPhoneEdt.text.toString()
                    court = courtNameEdt.text.toString()
                    hasFileAmount =
                        hasFileAmountEdt.text.toString().isEmpty().no { hasFileAmountEdt.text.toString().toDouble() }
                            .otherwise { 0.0 }
                    noFileAmount =
                        noFileAmountEdt.text.toString().isEmpty().no { noFileAmountEdt.text.toString().toDouble() }
                            .otherwise { 0.0 }
                    interest = rate / 100
                    wsFlag = hasFile.yes { "0" }.otherwise { "1" }
                    amount = hasFile.yes { hasFileAmount }.otherwise { noFileAmount }
                    judgmentTime = dateTv.text.toString()
                }

            }
        }
        return list
    }

    fun setPic(url: String, position: Int, id: String) {
        list[position].img = url
        list[position].imgId = id
        recyclerView.isFocusableInTouchMode = false
        notifyDataSetChanged()
    }
}