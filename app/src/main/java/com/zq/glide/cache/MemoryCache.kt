package com.zq.glide.cache

import androidx.collection.LruCache
import com.zq.glide.resource.Value

/**
 * @program: glide
 *
 * @description: LRU 内存缓存  回收机制 lru 算法
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 11:44
 **/

class MemoryCache(maxSize: Int) : LruCache<String, Value>(maxSize) {

    var memoryCacheCallback: MemoryCacheCallback? = null

    var shoudonRemove = false

    /**
     * 手动移除元素
     */
    fun shoudonRemove(key: String): Value? {
        shoudonRemove = true
        val remove = remove(key)
        shoudonRemove = false
        return remove
    }


    /**
     * 代表被移除了
     *
     */
    override fun entryRemoved(evicted: Boolean, key: String, oldValue: Value, newValue: Value?) {
        super.entryRemoved(evicted, key, oldValue, newValue)
        memoryCacheCallback?.let {
            if (!shoudonRemove) {
                it.entryRemoved(key, oldValue)  //被动移除
            }
        }
    }

    /**
     * 每一个元素的大小
     */
    override fun sizeOf(key: String, value: Value): Int {
        return value.bitmap.allocationByteCount
    }
}