package com.zq.glide.load

import com.zq.glide.resource.Value

/**
 * @program: glide
 *
 * @description: 加载外部资源 成功 和 失败 回调
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 15:14
 **/
interface IResponseListener {

    fun onSuccess(value: Value)

    fun onError(exception: Exception)
}