import android.content.Context
import android.content.SharedPreferences
import com.hotokay.jakeefactory.ui.base.GsonUtil
import com.wbg.xigui.bean.UserInfoBean
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * <p>
 * 作者：jakee
 * 创建时间：2018/11/21
 */
class Preference<R, T>(
    context: Context,
    private val key: String,
    private val default: T,
    private val clazz: Class<T>
) : ReadWriteProperty<R, T> {
    private val sharePreference: SharedPreferences by lazy {
        context.getSharedPreferences(
            "data",
            Context.MODE_PRIVATE
        )
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return findPreference()
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        putPreference(value)
    }

    private fun <T> findPreference(): T {
        with(sharePreference) {
            val value = when (default) {
                is Long -> getLong(key, default ?: 0)
                is String -> getString(key, default ?: "")
                is Int -> getInt(key, default ?: 0)
                is Float -> getFloat(key, default ?: 0.0f)
                is Boolean -> getBoolean(key, default ?: false)
                else -> GsonUtil.toModel(getString(key, "{}") ?: "{}", clazz)
            }
            return value as T
        }

    }

    private fun putPreference(value: T) {
        with(sharePreference.edit()) {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                else -> putString(key, GsonUtil.toJson(value))
            }.apply()
        }
    }

    companion object {
        fun <T> getList(context: Context, clazz: Class<T>): MutableList<T> {
            val sharePreference: SharedPreferences =
                context.getSharedPreferences(
                    "data",
                    Context.MODE_PRIVATE
                )

            with(sharePreference) {
                //                return GsonUtil.parseArray(getString(clazz.name, "[]"), clazz)
                return GsonUtil.toList(getString(clazz.name, "[]") ?: "[]", clazz)
            }
        }

        inline fun <reified T> putList(context: Context, list: MutableList<T>?) {
            (list != null).yes {
                val sharePreference: SharedPreferences =
                    context.getSharedPreferences(
                        "data",
                        Context.MODE_PRIVATE
                    )

                with(sharePreference.edit()) {
                    putString(T::class.java.name, GsonUtil.toJson(list))
                }.commit()
            }
        }

        fun <T> getModel(context: Context, clazz: Class<T>): T {
            val sharePreference: SharedPreferences by lazy {
                context.getSharedPreferences(
                    "data",
                    Context.MODE_PRIVATE
                )
            }
            with(sharePreference) {
                return GsonUtil.toModel(getString(clazz.name, "{}") ?: "{}", clazz)
            }
        }

        inline fun <reified T> putModel(context: Context, model: T) {
            val sharePreference: SharedPreferences by lazy {
                context.getSharedPreferences(
                    "data",
                    Context.MODE_PRIVATE
                )
            }
            with(sharePreference.edit()) {
                putString(T::class.java.name, GsonUtil.toJson(model))
            }.commit()
        }

        fun clearUserInfo(context: Context) {
            val sharePreference: SharedPreferences by lazy {
                context.getSharedPreferences(
                    "data",
                    Context.MODE_PRIVATE
                )
            }
            with(sharePreference.edit()) {
                putString(UserInfoBean::class.java.name, "{}")
            }.commit()
        }

    }
}