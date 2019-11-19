package com.wbg.xigui.utils

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener

/**
 * @author tyk
 * @date :2019/9/16 9:06
 * @des :  选择器工具
 */

class PickerViewUtil {

    companion object{
        /**
         * 显示一级选择器
         */
         fun showOnePickerView(context: Context,title:String, textView: TextView, list: MutableList<String>){
            val pvOptions = OptionsPickerBuilder(context,
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
    }

}