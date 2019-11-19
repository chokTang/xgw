package com.wbg.xigui.ui.near

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.gcssloop.widget.PagerGridLayoutManager
import com.gcssloop.widget.PagerGridSnapHelper
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.TypeAdapter
import com.wbg.xigui.base.BaseXFragment
import com.wbg.xigui.databinding.NearFragmentBinding
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.viewmodel.NearViewModel
import kotlinx.android.synthetic.main.near_fragment.*
import log
import pub.devrel.easypermissions.EasyPermissions
import yes
import java.io.Serializable

class NearFragment : BaseXFragment<NearViewModel>(), EasyPermissions.PermissionCallbacks
        , PagerGridLayoutManager.PageListener, RadioGroup.OnCheckedChangeListener {


    var adapter: TypeAdapter? = null


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        mLocationClient?.restart()
    }

    private var needRequestPermissions = true
    private var mLocationClient: LocationClient? = null
    private var myListener = (object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            location?.run {
                val latitude = latitude    //获取纬度信息
                val longitude = longitude    //获取经度信息
                val radius = radius    //获取定位精度，默认值为0.0f

                val coorType = coorType
                //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

                val errorCode = locType
                //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
                if (errorCode == 61 || errorCode == 161 || errorCode == 66) {
                    XApplication.mLocation = location
                    mViewModel.getStore()
                } else {
                    needRequestPermissions.yes { requestPermissions() }
                }
            }
        }

    })

    override fun initImmersionBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
    }

    var binding: NearFragmentBinding? = null
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initData() {
        binding?.run {
            mViewModel.getData()
            if (XApplication.mLocation == null) {
                initLocation()
                mLocationClient?.start()
            }
            refresh_layout.isEnableLoadMore = false
            refresh_layout.setOnRefreshListener {
                mViewModel.getData()
                viewPager()
                Handler().postDelayed({
                    refreshLayout.finishRefresh()
                }, 1000)
            }
        }

        viewPager()
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = bindLayout(inflater, R.layout.near_fragment, container)
        binding?.run {
            model = mViewModel
        }
        return mRootView
    }

    private fun requestPermissions() {
        needRequestPermissions = false
        val perms = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (!EasyPermissions.hasPermissions(context, *perms)) {
            EasyPermissions.requestPermissions(this, "为保证APP正常使用，请允许定位等权限", 101, *perms)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(101, permissions, grantResults, this)
    }


    fun viewPager() {
        mViewModel.getType {
            // 1.水平分页布局管理器
            val layoutManager = PagerGridLayoutManager(2, 4, PagerGridLayoutManager.HORIZONTAL)
            layoutManager.setPageListener(this)
            rv_type.layoutManager = layoutManager
            adapter = TypeAdapter()
            rv_type.adapter = adapter
            // 2.设置滚动辅助工具
            val pageSnapHelper = PagerGridSnapHelper()
            pageSnapHelper.attachToRecyclerView(rv_type)


            adapter?.setNewData(it)
            adapter?.setOnItemClickListener { adapter, view, position ->
                ARouter.getInstance().build(RoutUrl.Near.type_store)
                        .withInt("position", position)
                        .withSerializable("list", adapter?.data as Serializable).navigation()
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        log("onCheckedChanged$checkedId")
    }

    override fun onPageSelect(pageIndex: Int) {
        log("选中页码 = $pageIndex")
        indicator_container.setPosition(pageIndex)

    }

    override fun onPageSizeChanged(pageSize: Int) {
        log("总页数 =$pageSize")
    }

    fun initLocation() {
        mLocationClient = LocationClient(context)
        mLocationClient?.registerLocationListener(myListener)
        val option = LocationClientOption()

        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll")
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(0)
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.isOpenGps = true
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.isLocationNotify = true
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false)
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false)
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000)
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false)
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        mLocationClient?.locOption = option
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient?.start()
    }


}