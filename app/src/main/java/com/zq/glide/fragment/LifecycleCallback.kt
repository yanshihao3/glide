package com.zq.glide.fragment

/**
 * @program: glide
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 13:46
 **/
interface LifecycleCallback {
    fun glideStart()

    fun glideStop()

    fun glideDestroy()

}