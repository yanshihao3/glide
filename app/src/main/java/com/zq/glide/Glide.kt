package com.zq.glide

import android.content.Context
import androidx.fragment.app.FragmentActivity

/**
 * @program: glide
 *
 * @description: 手写简写glide
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 13:37
 **/
class Glide(val retriver: RequestManagerRetriver) {

    companion object {
        @JvmStatic
        fun with(fragmentActivity: FragmentActivity): RequestManager {
            return get(fragmentActivity).retriver[fragmentActivity]
        }

        @JvmStatic
        fun with(context: Context): RequestManager {
            return get(context).retriver[context]

        }

        @JvmStatic
        private fun get(context: Context): Glide {
            return Builder().build()
        }

        @JvmStatic
        private fun getRetriever(context: Context): RequestManagerRetriver {
            return get(context).retriver
        }
    }
}

/**
 * 有很多参数的时候 ，可以添加
 */
class Builder {

    fun build(): Glide {
        return Glide(RequestManagerRetriver())
    }
}