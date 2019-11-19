
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.wbg.xigui.BuildConfig
import com.wbg.xigui.XApplication
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

/**
 * <p>
 * 作者：jakee
 * 创建时间：2018/11/21
 */
fun <T> Observable<T>.netDispatch(observer: Observer<T>) {
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun toast(msg: String) {
    Toast.makeText(XApplication.instance, msg, Toast.LENGTH_SHORT).show()
}

fun log(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e("===", msg)
}


/*路由扩展*/
fun startRout(path: String) {
    ARouter.getInstance().build(path).navigation()
}

fun startRout(path: String, key: String, value: Any) {
    var temp = ARouter.getInstance().build(path)
    temp.also {
        when (value) {
            is CharSequence -> {
                temp.withString(key, value.toString())
            }
            is Int -> {
                temp.withInt(key, value)
            }
            is Boolean -> {
                temp.withBoolean(key, value)

            }
            is Serializable -> {
                temp.withSerializable(key, value)
            }
        }
    }.navigation()

}

fun startRout(path: String, keyBundle:String,bundle: Bundle) {
    ARouter.getInstance().build(path).withBundle(keyBundle,bundle).navigation()
}

sealed class BooleanExt<out T>(boolean: Boolean)
object Otherwise : BooleanExt<Nothing>(false)
class WithData<out T>(val data: T) : BooleanExt<T>(true)

inline fun <T> Boolean.yes(block: () -> T): BooleanExt<T> = when {
    this -> {
        WithData(block())
    }
    else -> Otherwise
}

inline fun <T> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

inline infix fun <T> BooleanExt<T>.otherwise(block: () -> T): T {
    return when (this) {
        is Otherwise -> block()
        is WithData<T> -> this.data
        else -> {
            throw IllegalAccessException()
        }
    }
}
inline operator fun <T> Boolean.invoke(block: () -> T) = yes(block)