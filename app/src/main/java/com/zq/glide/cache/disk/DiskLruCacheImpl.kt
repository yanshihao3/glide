package com.zq.glide.cache.disk

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.zq.glide.resource.Value
import com.zq.glide.resource.Value.Companion.instance
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * 我们自己的 磁盘缓存的封装
 * 目的：put  把我们的Value 存储进去
 * get 通过key 得到 Value
 */
class DiskLruCacheImpl {
    private val TAG = DiskLruCacheImpl::class.java.simpleName

    // Sdcard/disk_lru_cache_dir/ac037ea49e34257dc5577d1796bb137dbaddc0e42a9dff051beee8ea457a4668/缓存东西（Value）
    private val DISKLRU_CACHE_DIR = "disk_lru_cache_dir" // 磁盘缓存的的目录
    private val APP_VERSION = 1 // 我们的版本号，一旦修改这个版本号，之前的缓存失效
    private val VALUE_COUNT = 1 // 通常情况下都是1
    private val MAX_SIZE = (1024 * 1024 * 10).toLong() // 以后修改成 使用者可以设置的
    private var diskLruCache: DiskLruCache? = null

    init {
        // SD 路径
        val file = File(
            Environment.getExternalStorageDirectory()
                .absolutePath + File.separator + DISKLRU_CACHE_DIR
        )
        try {
            diskLruCache = DiskLruCache.open(file, APP_VERSION, VALUE_COUNT, MAX_SIZE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // TODO put
    fun put(key: String, value: Value) {
        var editor: DiskLruCache.Editor? = null
        var outputStream: OutputStream? = null
        try {
            editor = diskLruCache!!.edit(key)
            outputStream = editor.newOutputStream(0) // index 不能大于 VALUE_COUNT
            val bitmap = value.bitmap
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // 把bitmap写入到outputStream
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            // 失败
            try {
                editor!!.abort()
            } catch (e1: IOException) {
                e1.printStackTrace()
                Log.e(TAG, "put: editor.abort() e:" + e.message)
            }
        } finally {
            try {
                editor!!.commit() // sp 记得一定要提交
                diskLruCache!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "put: editor.commit(); e:" + e.message)
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(TAG, "put: outputStream.close(); e:" + e.message)
                }
            }
        }
    }

    // TODO get
    operator fun get(key: String): Value? {
        var inputStream: InputStream? = null
        try {
            val snapshot = diskLruCache!![key]
            // 判断快照不为null的情况下，在去读取操作
            if (null != snapshot) {
                val value = instance
                inputStream = snapshot.getInputStream(0) // index 不能大于 VALUE_COUNT
                val bitmap = BitmapFactory.decodeStream(inputStream)
                value.bitmap = bitmap
                // 保存key 唯一标识
                value.key = key
                return value
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(TAG, "get: inputStream.close(); e:" + e.message)
                }
            }
        }
        return null // 为了后续好判断
    }


}