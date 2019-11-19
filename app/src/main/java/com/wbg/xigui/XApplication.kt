package com.wbg.xigui

import Preference
import android.app.Activity
import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.location.LocationClientOption.LocationMode
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import com.wbg.xigui.bean.UserInfoBean
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.utils.UtilApp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import log
import otherwise
import yes
import java.util.concurrent.TimeUnit


class XApplication : Application() {
    private var activityList = ArrayList<Activity>()
    private var mLocationClient: LocationClient? = null
    private var myListener = MyLocationListener()
    override fun onCreate() {
        super.onCreate()
        //极光推送
        JPushInterface.setDebugMode(false)
        JPushInterface.init(this)

        instance = this
        ZXingLibrary.initDisplayOpinion(this)
        init()
        getLocatoin()
        UtilApp.getIntance().init(this)
    }

    companion object {
        lateinit var instance: XApplication
        lateinit var currentActivity: Activity
        private var role: String? = null//临时角色
        private var userInfo: UserInfoBean? = null
        private var memberId: String? = null
        private var token: String? = null
        var mLocation: BDLocation? = null
        fun isLogin(): Boolean {
            return !instance.getToken().isNullOrEmpty()
        }
    }

    fun logout() {
        setToken("")
        setUserInfo(null)
    }

    fun getRole(): String? {
        return role
    }

    fun setRole(mRole: String?) {
        role = mRole
    }

    fun getToken(): String? {
        if (token.isNullOrEmpty()) {
            token = getUserInfo()?.token
        }
        return token
    }

    fun setToken(str: String?) {
        token = str
    }

    fun getMemberId(): String? {
        if (memberId.isNullOrEmpty()) {
            memberId = getUserInfo()?.member?.id
        }
        return memberId
    }

    fun setMemberId(str: String?) {
        memberId = str
    }


    private fun initSmartLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context).setColorSchemeColors(resources.getColor(R.color.colorPrimary))//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //指定为经典Footer，默认是 BallPulseFooter
            BallPulseFooter(context).setAnimatingColor(resources.getColor(R.color.colorPrimary))
                .setIndicatorColor(resources.getColor(R.color.colorPrimary))
        }
    }

    private fun init() {
        ResourcesUtil.init(resources)
        initSmartLayout()
        initRout()
        initLocation()
    }

    fun initLocation() {
        mLocationClient = LocationClient(this)
        mLocationClient?.registerLocationListener(myListener)
        val option = LocationClientOption()

        option.locationMode = LocationMode.Hight_Accuracy
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
    }

    fun setUserInfo(info: UserInfoBean?) {
        userInfo = info
        Preference.putModel(this, info ?: UserInfoBean())
    }

    fun getUserInfo(): UserInfoBean {
        if (userInfo == null) {
            userInfo = Preference.getModel(this, UserInfoBean::class.java)
        }
        return userInfo!!
    }



    private fun initRout() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    fun getLocatoin() {
        mLocationClient?.start()
        Observable.interval(60 * 3, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : io.reactivex.Observer<Long?> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Long) {
                    mLocationClient?.restart()
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun addActivity(activity: Activity) {
        if (BuildConfig.DEBUG){
            log("${Thread.currentThread()}addActivity(activity: Activity)")
        }
        synchronized(activityList) {
            activityList.add(0, activity)
        }
    }

    fun removeActivity(activity: Activity) {
        synchronized(activityList) {
            activityList.remove(activity)
        }
    }

    //关闭所有activity
    fun clearActivity() {
        synchronized(activityList) {
            activityList.forEach { it.finish() }
            activityList.clear()
        }
    }

    //关闭某个activity
    fun finishActivity(clazz: Class<*>) {
        synchronized(activityList) {
            activityList = activityList.filterNot {
                clazz.isInstance(it).yes {
                    it.finish()
                    true
                }.otherwise { false }
            } as ArrayList<Activity>
        }
    }

    //关闭所有 除了某个activity
    fun finishAllActivity(clazz: Class<Activity>) {
        synchronized(activityList) {
            activityList = activityList.filter {
                clazz.isInstance(it).yes {
                    true
                }.otherwise {
                    it.finish()
                    false
                }
            } as ArrayList<Activity>
        }
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
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
                    mLocation = location
                    mLocationClient?.stop()
                }
                log("latitude:$latitude--longitude:$longitude")
            }

        }

    }
}