package com.zq.glide.cache

import com.zq.glide.resource.Value
import com.zq.glide.resource.ValueCallback
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

/**
 * @program: glide
 *
 * @description: 活动缓存
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 10:57
 **/
/**
 * 容器   添加  移除
 */
class ActiveCache(private val valueCallback: ValueCallback) {

    private val mHashMap by lazy { mutableMapOf<String, CustomWeakReference>() }

    lateinit var mQueue: ReferenceQueue<Value>

    private var isCloseThread = true

    private lateinit var thread: Thread

    /**
     * 添加
     */
    public fun put(key: String, value: Value) {
        value.valueCallback = valueCallback
        //存储到容器
        mHashMap[key] = CustomWeakReference(value, getQueue(), key)
    }

    public fun remove(key: String): Value? {
        val remove = mHashMap.remove(key)
        if (remove != null) {
            return remove.get()
        }
        return null
    }

    /**
     * 获取
     */
    operator fun get(key: String): Value? {
        return mHashMap[key]?.get()
    }

    /**
     * 关闭 线程 释放资源
     */
    fun closeThread() {
        isCloseThread = false
        if (::thread.isInitialized) {
            thread.interrupt()
        }
        mHashMap.clear()
    }

    private fun getQueue(): ReferenceQueue<Value> {
        if (!::mQueue.isInitialized) {
            mQueue = ReferenceQueue()
            thread = thread {
                while (isCloseThread) {
                    try {
                        val remove = mQueue.remove()
                        remove as CustomWeakReference
                        mHashMap.remove(remove.key)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return mQueue
    }

    /**
     * 弱引用回收监听
     */
    class CustomWeakReference(
        private val value: Value,
        private val queue: ReferenceQueue<Value>,
        val key: String
    ) : WeakReference<Value>(value, queue) {

    }
}