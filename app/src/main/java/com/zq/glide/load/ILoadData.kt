package com.zq.glide.load

import android.content.Context
import com.zq.glide.resource.Value

/**
 * @program: glide
 *
 * @description: 加载外部资源
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 15:13
 **/
interface ILoadData {

    fun loadResource(path: String, responseListener: IResponseListener, context: Context): Value?
}