package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.CityBean
import com.wbg.xigui.bean.ParmasAddress
import com.wbg.xigui.bean.X
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GetJsonDataUtil
import com.wbg.xigui.viewmodel.AddAddressViewModel
import kotlinx.android.synthetic.main.activity_add_address.*
import org.json.JSONArray
import toast
import java.util.*

/**
 * @author tyk
 * @date :2019/8/16 14:44
 * @des :  添加地址  修改地址
 */
@Route(path = RoutUrl.Mine.add_address)
class AddAddressActivity : BaseXActivity<AddAddressViewModel>(), View.OnClickListener {

    companion object {
        const val MSG_LOAD_DATA = 0x0001
        const val MSG_LOAD_SUCCESS = 0x0002
        const val MSG_LOAD_FAILED = 0x0003
        const val KEY_ADDRESS_BEAN = "address_bean"
        const val KEY_BUNDLE = "bundle"
    }

    private var thread: Thread? = null
    private var options1Items: List<CityBean> = ArrayList<CityBean>()
    private val options2Items = ArrayList<ArrayList<String>>()
    private val options3Items = ArrayList<ArrayList<ArrayList<String>>>()
    var cityCode = ""
    var province = ""
    var city = ""
    var region = ""
    var addressId = ""
    var addressBean: X? = null
    private var isLoaded = false


    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("添加地址")
        addView(R.layout.activity_add_address)
        setListener()
    }

    fun setListener() {
        tv_confirm.setOnClickListener(this)
        edt_address.setOnClickListener(this)
        ll_select.setOnClickListener(this)
    }

    override fun initData() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA)
        if (null != intent.getBundleExtra(KEY_BUNDLE)) {
            addressBean = intent.getBundleExtra(KEY_BUNDLE).get(KEY_ADDRESS_BEAN) as X?
        }
        if (addressBean != null) {//编辑 修改
            edt_name.text = Editable.Factory.getInstance().newEditable(addressBean?.name)
            edt_phone.text = Editable.Factory.getInstance().newEditable(addressBean?.phoneNumber)
            edt_address.text = Editable.Factory.getInstance()
                .newEditable(addressBean?.province + addressBean?.city + addressBean?.region)
            edt_address_detail.text = Editable.Factory.getInstance().newEditable(addressBean?.detailAddress)
            edt_mail.text = Editable.Factory.getInstance().newEditable(addressBean?.postCode)
            switch_btn.isSelected = addressBean?.defaultStatus == "1"
            province = addressBean?.province!!
            city = addressBean?.city!!
            region = addressBean?.region!!
            cityCode = addressBean?.cityCode!!
            addressId = addressBean?.id!!
        } else {//新添加
            //暂时不做操作
            addressId = ""
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                addBean()
            }

            R.id.edt_address -> {
                if (isLoaded) {
                    showPickerView()
                }
            }
            R.id.ll_select->{
                switch_btn.isSelected = true
            }
        }
    }

    /**
     * 请求方法
     */
    fun addBean() {
        if (TextUtils.isEmpty(edt_name.text.toString())) {
            toast("请填写收货人姓名")
            return
        }
        if (TextUtils.isEmpty(edt_phone.text.toString())) {
            toast("请填写收货人电话")
            return
        }
        if (TextUtils.isEmpty(edt_address.text.toString())) {
            toast("请选择省市区")
            return
        }
        if (TextUtils.isEmpty(edt_address_detail.text.toString())) {
            toast("请填写详情地址")
            return
        }

        if (TextUtils.isEmpty(edt_mail.text.toString())) {
            toast("请填写邮政编码")
            return
        }

        val bean = ParmasAddress()
        bean.addressId = addressId
        bean.name = edt_name.text.toString()
        bean.detailAddress = edt_address_detail.text.toString()
        bean.memberId = XApplication.instance.getMemberId()
        bean.phoneNumber = edt_phone.text.toString()
        bean.postCode = edt_mail.text.toString()
        bean.cityCode = cityCode
        bean.province = province
        bean.city = city
        bean.region = region
        if (switch_btn.isSelected) {
            bean.defaultStatus = "1"
        } else {
            bean.defaultStatus = "0"
        }
        mViewModel.addNewOrUpdateAddress(bean) {
            finish()
        }
    }


    private fun showPickerView() {// 弹出选择器
        val pvOptions = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1, options2, options3, v ->
                //返回的分别是三个级别的选中位置
                val opt1tx = if (options1Items.size > 0)
                    options1Items[options1].name
                else
                    ""

                val opt2tx = if (options2Items.size > 0 && options2Items[options1].size > 0)
                    options2Items[options1][options2]
                else
                    ""

                val opt3tx = if (options2Items.size > 0
                    && options3Items[options1].size > 0
                    && options3Items[options1][options2].size > 0
                )
                    options3Items[options1][options2][options3]
                else
                    ""

                val tx = opt1tx + opt2tx + opt3tx
                province = options1Items[options1].name!!
                city = options1Items[options1].children!![options2]!!.name!!
                region = options1Items[options1].children!![options2]!!.children!![options3]!!.name!!
                //这里吧位置 当做code码 是因为code仅仅是用来 编辑修改的时候 默认位置选中以前选中的位置
                cityCode = "$options1,$options2,$options3"
                edt_address.text = tx
            })

            .setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .build<Any>()


        pvOptions.setPicker(
            options1Items, options2Items as List<MutableList<Any>>?,
            options3Items as List<MutableList<MutableList<Any>>>?
        )//三级选择器
        if (addressBean != null) {
            val options = addressBean?.cityCode?.split(",")
            pvOptions.setSelectOptions(options!![0].toInt(), options[1].toInt(), options[2].toInt())
        }
        pvOptions.show()
    }


    private fun initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         */
        val JsonData = GetJsonDataUtil().getJson(this, "address.json")//获取assets目录下的json文件数据

        val jsonBean = parseData(JsonData)//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean

        for (i in jsonBean.indices) {//遍历省份
            val cityList = ArrayList<String>()//该省的城市列表（第二级）

            val province_AreaList = ArrayList<ArrayList<String>>()//该省的所有地区列表（第三极）

            for (c in 0 until jsonBean[i].children!!.size) {//遍历该省份的所有城市
                cityList.add(jsonBean[i].children?.get(c)!!.name!!)//添加城市
                val city_AreaList: ArrayList<String> = arrayListOf()//该城市的所有地区列表

                for (j in 0 until jsonBean[i].children?.get(c)!!.children!!.size) {
                    city_AreaList.add(jsonBean[i].children?.get(c)!!.children!![j]!!.name!!)
                }
                province_AreaList.add(city_AreaList)//添加该省所有地区数据

            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList)

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList)
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS)

    }

    /**
     * 解析地址数据
     */
    fun parseData(result: String): ArrayList<CityBean> {//Gson 解析
        val detail = ArrayList<CityBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity: CityBean =
                    gson.fromJson<Any>(data.optJSONObject(i).toString(), CityBean::class.java) as CityBean
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED)
        }
        return detail
    }


    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_LOAD_DATA -> if (thread == null) {//如果已创建就不再重新创建子线程了
                    thread = Thread(Runnable {
                        // 子线程中解析省市区数据
                        initJsonData()
                    })
                    thread!!.start()
                }
                MSG_LOAD_SUCCESS -> {
                    isLoaded = true
                }
                MSG_LOAD_FAILED -> {
                    toast("地址数据解析错误")
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}