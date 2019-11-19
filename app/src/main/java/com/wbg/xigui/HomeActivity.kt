package com.wbg.xigui

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.kw.rxbus.RxBus
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bus.RxEvent
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.main.HomeFragment
import com.wbg.xigui.ui.mine.MineFragment
import com.wbg.xigui.ui.near.NearFragment
import com.wbg.xigui.ui.shopcart.ShopCartFragment
import com.wbg.xigui.ui.type.TypeFragment
import com.wbg.xigui.viewmodel.HomeViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.home_activity.*
import pub.devrel.easypermissions.EasyPermissions

@Route(path = RoutUrl.Main.home_activity)
class HomeActivity : BaseXActivity<HomeViewModel>() {
    companion object {
        const val KEY_POSITION = "position"
    }

    private var currentFragment: Fragment? = null
    var binding: com.wbg.xigui.databinding.HomeActivityBinding? = null
    var tabs: Array<View>? = null
    var position = 0
    var disposable: Disposable? = null

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        hideTitle()
        binding = bindLayout(R.layout.home_activity)
        tabs = arrayOf(home_tv, near_tv, type_tv, shop_cart_tv, mine_tv)
        position = intent.getIntExtra(KEY_POSITION, 0)
    }

    override fun initData() {
        requestPermissions()
        binding?.run {
            model = mViewModel
            mViewModel.fragmentType.observe(this@HomeActivity, Observer {
                replaceFragment(it)
            })
            replaceFragment(0)
        }
    }

    fun setSelectedView(index: Int) {
        tabs?.run {
            forEach { it.isSelected = false }
            this[index].isSelected = true
        }

    }

    override fun onResume() {
        super.onResume()
        disposable = RxBus.getInstance().toObservable(RxEvent::class.java).subscribe {
            position = it.type
            mViewModel.changeFragment(position)
        }
    }

    private fun replaceFragment(type: Int) {
        setSelectedView(type)
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
        }
        currentFragment = supportFragmentManager.findFragmentByTag("flag$type")
        var bundle = Bundle()
        bundle.putInt("type", type)
        if (currentFragment == null) {
            when (type) {
                0 -> {
                    currentFragment = HomeFragment()
                }
                1 -> {
                    currentFragment = NearFragment()
                }
                2 -> {
                    currentFragment = TypeFragment()
                }
                3 -> {
                    currentFragment = ShopCartFragment()
                }
                4 -> {
                    currentFragment = MineFragment()
                }
            }

            currentFragment!!.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.content, currentFragment!!, "flag$type").commit()
            supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()//先hide一次  触发懒加载逻辑
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        } else {
            supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
        }
    }

    private fun requestPermissions() {
        val perms = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        )
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            EasyPermissions.requestPermissions(this, "为保证APP正常使用，请允许存储等权限", 101, *perms)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister(disposable)
    }
}