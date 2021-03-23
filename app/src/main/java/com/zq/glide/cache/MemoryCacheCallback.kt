package com.zq.glide.cache

import com.zq.glide.resource.Value

/**
 * @program: glide
 *
 * @description: 内存缓存中 ，LRU 移除 回调的接口
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 11:51
 **/
interface MemoryCacheCallback {

    fun entryRemoved(key: String, value: Value)
}