package com.zq.glide

import android.content.Context

import androidx.fragment.app.FragmentActivity


/**
 * @program: glide
 *
 * @description: 管理 RequestManager
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 14:12
 **/
class RequestManagerRetriver {

    operator fun get(fragmentActivity: FragmentActivity): RequestManager { // this
        return RequestManager(fragmentActivity)
    }


    operator fun get(context: Context): RequestManager {
        return RequestManager(context)
    }

}