package com.wbg.xigui.adapter


import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(fm: FragmentManager, private val list: MutableList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt("type", position)
        list[position].arguments = bundle
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //        super.destroyItem(container, position, object);
    }
}
