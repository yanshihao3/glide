package com.zq.glide.resource

import com.zq.glide.utils.ShaUtil

/**
 * @program: glide
 *
 * @description: key
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-22 19:18
 **/
class Key(var key: String) {
    init {
        key = ShaUtil.SHA256(key)
    }
}