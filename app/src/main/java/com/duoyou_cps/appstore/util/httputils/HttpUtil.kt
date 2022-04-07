import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.duoyou.develop.entity.UserInfo
import com.duoyou.develop.utils.AppInfoUtils
import com.duoyou_cps.appstore.entity.BaseDataBean
import com.duoyou_cps.appstore.util.LoginImplManager
import com.duoyou_cps.appstore.util.httputils.errorCode
import com.duoyou_cps.appstore.util.httputils.errorMsg
import com.duoyou_cps.appstore.util.httputils.getUrlWithHost
import com.duoyou_cps.appstore.util.httputils.show
import com.duoyou_cps.appstore.util.logi
import com.google.gson.Gson
import com.tencent.smtt.utils.m
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import rxhttp.wrapper.param.AbstractParam
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.RxHttpJsonParam
import rxhttp.wrapper.param.RxHttpNoBodyParam
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *  大部分请求均可以使用这个
 *  data字段为object时可用
 */

fun <T> httpRequest(
    url: String, params: MutableMap<String, Any?>,
    clazz: Class<T>,
    callback: HttpCallback<T>?
) {
    httpRequest(url, params, clazz, false, callback)
}

fun <T> httpRequest(
    url: String, params: MutableMap<String, Any?>,
    clazz: Class<T>, get: Boolean,
    callback: HttpCallback<T>?
) {
    getObserveOn(url, params, clazz, get)
        .doFinally { callback?.onFinish() }
        .subscribe({
            callback?.onSuccess(it)
        }) {
            callback?.onFailed(it.errorCode, it.errorMsg)
        }
}

private fun <T> getObserveOn(
    url: String, params: MutableMap<String, Any?>?,
    clazz: Class<T>, isGet: Boolean
): @NonNull Observable<T> {
    return (if (isGet) RxHttp.get(getUrlWithHost(url)).addAll(params)
    else RxHttp.postJson(getUrlWithHost(url)).addAll(params))
        .asResponse(clazz)
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 *  针对data字段为list时使用
 */
//fun <T> postRequestList(
//    url: String,
//    params: MutableMap<Any, Any>,
//    clazz: Class<T>,
//    callback: HttpCallback<List<T>>
//) {
//    getObservable(url, params)
//        .doFinally { callback.onFinish() }
//        .subscribe({
//            try {
//                val fromJson = fromJsonArray(it, clazz)
//                fromJson?.let { response ->
//                    if (response.status_code == 200) {
//                        response.data?.let { data ->
//                            callback.onSuccess(data)
//                        } ?: run { callback.onSuccess(arrayListOf()) }
//                        return@let
//                    }
//                    callback.onFailed(response.status_code, response.message)
//                }
//            } catch (e: Exception) {
//                callback.onFailed(-1, e.message)
//            }
//
//        }) {
//            callback.onFailed(0, it.message)
//        }
//
//
//}

//
inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, T::class.java)
}


//fun getPostObservable(url: String, params: MutableMap<Any, Any>?): Observable<String> {
//
//    logi("json__request", ":url=${getUrlWithHost(url)} params=${GsonUtils.toJson(params)}")
//    return RxHttp.postJson(getUrlWithHost(url))
//        .addAll(GsonUtils.toJson(addCommonParam(params)))
//        .asString()
//        .observeOn(AndroidSchedulers.mainThread())
//}


//fun addCommonParam(params: MutableMap<String, Any?>?): MutableMap<String, Any?>? {
//    params?.let {
//        if (UserInfo.getInstance().isLogin) {
//            it["access_token"] = UserInfo.getInstance().accessToken
//        }
//        it["channel"] = AppInfoUtils.getChannel()
//    }
//    return params
//}


fun <T> fromJsonArray(json: String?, clazz: Class<T>): BaseDataBean<List<T>?>? {
    // 生成List<T> 中的 List<T>
    val listType: Type = ParameterizedTypeImpl(MutableList::class.java, arrayOf(clazz))
    // 根据List<T>生成完整的Result<List<T>>
    val type: Type = ParameterizedTypeImpl(BaseDataBean::class.java, arrayOf(listType))
    return Gson().fromJson(json, type)
}

fun <T> fromJsonObject(json: String?, clazz: Class<T>): BaseDataBean<T>? {
    val type = ParameterizedTypeImpl(BaseDataBean::class.java, arrayOf(clazz))
    return Gson().fromJson(json, type)
}

//
abstract class HttpCallback<T> {
    abstract fun onSuccess(data: T?)
    open fun onFailed(code: Int, msg: String?) {
        if (code == 403) {
            if (LoginImplManager.getLoginStatus()) {
                ToastUtils.showShort(msg)
                LoginImplManager.logout()
            }
            return
        }
        ToastUtils.showShort(msg)
    }

    open fun onFinish() {}
}

class ParameterizedTypeImpl(private val raw: Class<*>, var args: Array<Type>?) :
    ParameterizedType {
    init {
        args = args ?: arrayOf()
    }

    override fun getActualTypeArguments(): Array<Type>? {
        return args
    }

    override fun getRawType(): Type {
        return raw
    }

    override fun getOwnerType(): Type? {
        return null
    }

}
