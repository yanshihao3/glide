package com.zq.glide.resource

import android.graphics.Bitmap

/**
 * @program: glide
 *
 * @description: bitmap的封装
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-22 19:18
 **/
class Value {
    lateinit var bitmap: Bitmap

    var count: Int = 0

    var valueCallback: ValueCallback? = null

    var key: String? = null

    /**
     * 使用一次 计数一次 +1
     */
    fun useAction() {
        if (!::bitmap.isInitialized) {
            throw IllegalArgumentException("bitmap 为 空")
        }
        if (bitmap.isRecycled) {
            return
        }
        count++
    }

    fun nonUseAction() {
        if (count-- <= 0) { //证明我们的value没有使用
            valueCallback?.valueNonUseListener(key!!, this)
        }
    }

    fun recycleBitmap() {
        if (count > 0) return
        if (bitmap.isRecycled) return
        bitmap.recycle()
    }

    /**
     * 静态内部类实现单例
     */
    companion object {
        val instance = SingletonHolder.holder

        /**
         * 双重校验锁式（Double Check) 实现单例
         */
        val instance2: Value by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Value()
        }
        private const val TAG = "Value"
    }

    private object SingletonHolder {
        val holder = Value()
    }
}