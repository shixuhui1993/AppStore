package com.example.httpsender.parser

import com.duoyou_cps.appstore.entity.BaseDataBean
import com.duoyou_cps.appstore.entity.PageList
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type

@Parser(name = "Response", wrappers = [PageList::class])
open class ResponseParser<T> : TypeParser<T> {

    protected constructor() : super()

    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: okhttp3.Response): T {
        val data: BaseDataBean<T> =
            response.convertTo(BaseDataBean::class, *types)
        var t = data.data //获取data字段
        if (t == null && types[0] === String::class.java) {
            /*
             * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
             * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
             * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
             */
            @Suppress("UNCHECKED_CAST")
            t = data.message as T
        }
        if (data.status_code != 200 || t == null) { //code不等于0，说明数据不正确，抛出异常
            throw ParseException(data.status_code.toString(), data.message, response)
        }
        return t
    }
}