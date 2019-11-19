package com.wbg.xigui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.baidu.location.BDLocation
import toast
import java.util.*

object MapUtil {
    /**
     * 跳转高德地图
     */
    fun goToGaodeMap(context: Context, mLat: Double, mLng: Double, mAddressStr: String) {
        if (!isInstalled(context, "com.autonavi.minimap")) {
            toast("请先安装高德地图客户端")
            return
        }
        val latLng= bdToGaoDe(mLat,mLng)
        val stringBuffer = StringBuffer("androidamap://navi?sourceApplication=").append("amap")
        stringBuffer.append("&lat=").append(latLng[0])
            .append("&lon=").append(latLng[1])
            .append("&keywords=$mAddressStr")
            .append("&dev=").append(0)
            .append("&style=").append(2)
        val intent = Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()))
        intent.setPackage("com.autonavi.minimap")
        context.startActivity(intent)
    }

    /**
     * 跳转百度地图
     */
    fun goToBaiduMap(context: Context, mLat: Double, mLng: Double, mAddressStr: String) {
        if (!isInstalled(context, "com.baidu.BaiduMap")) {
            toast("请先安装百度地图客户端")
            return
        }
        val intent = Intent()
        intent.data = Uri.parse(
            "baidumap://map/direction?destination=latlng:"
                    + mLat + ","
                    + mLng + "|name:" + mAddressStr + // 终点

                    "&mode=driving" + // 导航路线方式

                    "&src=com.shoufu.ywf"
        )
        context.startActivity(intent) // 启动调用
    }

    /**
     * 跳转腾讯地图
     */
    fun goToTencentMap(context: Context, mLat: Double, mLng: Double, mAddressStr: String) {
        if (!isInstalled(context, "com.tencent.map")) {
            toast("请先安装腾讯地图客户端")
            return
        }
        var location=BDLocation()
        location.latitude=mLat
        location.longitude=mLng
        val endPoint = BD09ToGCJ02(location)//坐标转换
        val stringBuffer = StringBuffer("qqmap://map/routeplan?type=drive")
            .append("&tocoord=").append(endPoint[0]).append(",").append(endPoint[1])
            .append("&to=$mAddressStr")
        val intent = Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()))
        context.startActivity(intent)
    }
    fun BD09ToGCJ02(latLng: BDLocation): Array<Double> {
        val x_pi = 3.14159265358979324 * 3000.0 / 180.0
        val x = latLng.longitude - 0.0065
        val y = latLng.latitude - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi)
        val gg_lat = z * Math.sin(theta)
        val gg_lng = z * Math.cos(theta)
        return arrayOf(gg_lat, gg_lng)
    }
    /**
     * 百度转高德
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    fun bdToGaoDe(bd_lat: Double, bd_lon: Double): DoubleArray {
        val gd_lat_lon = DoubleArray(2)
        val PI = 3.14159265358979324 * 3000.0 / 180.0
        val x = bd_lon - 0.0065
        val y = bd_lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI)
        gd_lat_lon[0] = z * Math.cos(theta)
        gd_lat_lon[1] = z * Math.sin(theta)
        return gd_lat_lon
    }
    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        val manager = context.packageManager
        //获取所有已安装程序的包信息
        val installedPackages = manager.getInstalledPackages(0)
        if (installedPackages != null) {
            for (info in installedPackages) {
                if (info.packageName == packageName)
                    return true
            }
        }
        return false
    }

    fun goNav(mContext: Context, lat: Double, lng: Double, address: String) {
        val maps = ArrayList<String>()
        if (isInstalled(mContext, "com.baidu.BaiduMap")) {
            goToBaiduMap(mContext, lat, lng, address)
            return
        }
        if (isInstalled(mContext, "com.autonavi.minimap")) {
            goToGaodeMap(mContext, lat, lng, address)
            return
        }
        if (isInstalled(mContext, "com.tencent.map")) {
            goToTencentMap(mContext, lat, lng, address)
            return
        }
        if (maps.size == 0) {
            toast("没有找到相应的地图应用")
            return
        }
    }
}
