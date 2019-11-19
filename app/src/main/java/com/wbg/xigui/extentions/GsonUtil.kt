package com.hotokay.jakeefactory.ui.base

import com.google.gson.Gson
import com.google.gson.JsonParser


/**
 * <p>
 * 作者：jakee
 * 创建时间：2018/11/26
 */
class GsonUtil {

    companion object {
        private val gson: Gson by lazy { Gson() }
        fun <T> toJson(model: T): String {
            return gson.toJson(model)
        }

        fun <T> toList(listJson: String?, clazz: Class<T>): MutableList<T> {
            val list = ArrayList<T>()
            val array = JsonParser().parse(listJson?:"[]").asJsonArray
            for (elem in array) {
                list.add(gson.fromJson<T>(elem, clazz))
            }
            return list
        }

        fun <T> toModel(modelJson: String?, clazz: Class<T>): T {
            return gson.fromJson(modelJson?:"{}", clazz)
        }
    }
}
