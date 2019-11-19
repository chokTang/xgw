package com.wbg.xigui.utils

import java.util.*

/**
 * @author tyk
 * @date :2019/8/20 12:06
 * @des :
 */
object PamasUtil {
    fun objectToMap(obj: Any?): Map<String, Any>? {
        if (obj == null) {
            return null
        }

        val map = HashMap<String, Any>()
        try {
            val declaredFields = obj.javaClass.declaredFields
            for (field in declaredFields) {
                field.isAccessible = true
                map[field.name] = field.get(obj)
            }
        } catch (e: Exception) {

        }
        return map
    }
}
