package com.wbg.xigui.ui.mine

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.CityBean
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GetJsonDataUtil
import com.wbg.xigui.viewmodel.CommitDataViewModel
import kotlinx.android.synthetic.main.activity_commit_data.*
import org.json.JSONArray
import startRout
import toast
import java.util.*

/**
 * @author tyk
 * @date :2019/9/11 15:10
 * @des :  提交认证资料
 */
@Route(path = RoutUrl.Mine.commit_data)
class CommitDataActivity : BaseXActivity<CommitDataViewModel>(), View.OnClickListener {

    private var thread: Thread? = null
    private var options1Items: List<CityBean> = ArrayList<CityBean>()
    private val options2Items = ArrayList<ArrayList<String>>()
    private val options3Items = ArrayList<ArrayList<ArrayList<String>>>()
    private var isLoaded = false
    var cityCode = ""
    var province = ""
    var city = ""

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("提交认证资料")
        addView(R.layout.activity_commit_data)
    }

    override fun initData() {
        mHandler.sendEmptyMessage(AddAddressActivity.MSG_LOAD_DATA)
        setListener()
    }

    fun setListener() {
        tv_type.setOnClickListener(this)
        tv_jyfs.setOnClickListener(this)
        tv_zylm.setOnClickListener(this)
        tv_address.setOnClickListener(this)
        btn_next_step.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_type -> {//选择种类
                val list:MutableList<String> = arrayListOf()
                list.add("个人店铺")
                list.add("个体工商户店")
                showOnePickerView("店铺类型",tv_type,list)
            }
            R.id.tv_jyfs->{//经营方式
                val list:MutableList<String> = arrayListOf()
                list.add("线上")
                list.add("线下")
                list.add("线上/线下")
                showOnePickerView("经营方式",tv_jyfs,list)
            }
            R.id.tv_zylm->{//主营类目
                val list:MutableList<String> = arrayListOf()
                list.add("普通商品")
                list.add("虚拟商品")
                showOnePickerView("主营类目",tv_zylm,list)
            }
            R.id.tv_address -> {//选择区域
                showPickerView()
            }
            R.id.btn_next_step->{//下一步
                startRout(RoutUrl.Mine.commit_id_card_data)
            }
        }
    }

    /**
     * 显示一级选择器
     */
    private fun showOnePickerView(title:String,textView: TextView,list: MutableList<String>){
        val pvOptions = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1, options2, options3, v ->
                //返回的分别是三个级别的选中位置
                val opt1tx = if (list.isNotEmpty())
                    list[options1]
                else
                    ""
                val tx = opt1tx
                textView.text = tx
            })

            .setTitleText(title)
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .build<Any>()

        pvOptions.setPicker(list as List<Any>?)//一级选择器
        pvOptions.show()
    }

    /**
     * 显示三级选择器 地址
     */
    private fun showPickerView() {// 弹出选择器
        val pvOptions = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1, options2, options3, v ->
                //返回的分别是三个级别的选中位置
                val opt1tx = if (options1Items.isNotEmpty())
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
//                province = options1Items[options1].name!!
//                city = options1Items[options1].children!![options2]!!.name!!
//                region = options1Items[options1].children!![options2]!!.children!![options3]!!.name!!
                val tx = "$opt1tx/$opt2tx/$opt3tx"
                tv_address.text = tx
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

        mHandler.sendEmptyMessage(AddAddressActivity.MSG_LOAD_SUCCESS)

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
                    gson.fromJson<Any>(
                        data.optJSONObject(i).toString(),
                        CityBean::class.java
                    ) as CityBean
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mHandler.sendEmptyMessage(AddAddressActivity.MSG_LOAD_FAILED)
        }
        return detail
    }


    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                AddAddressActivity.MSG_LOAD_DATA -> if (thread == null) {//如果已创建就不再重新创建子线程了
                    thread = Thread(Runnable {
                        // 子线程中解析省市区数据
                        initJsonData()
                    })
                    thread!!.start()
                }
                AddAddressActivity.MSG_LOAD_SUCCESS -> {
                    isLoaded = true
                }
                AddAddressActivity.MSG_LOAD_FAILED -> {
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