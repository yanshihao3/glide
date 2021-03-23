package com.zq.glide

import android.content.Context
import android.os.Looper
import android.widget.ImageView
import com.zq.glide.cache.ActiveCache
import com.zq.glide.cache.MemoryCache
import com.zq.glide.cache.MemoryCacheCallback
import com.zq.glide.cache.disk.DiskLruCacheImpl
import com.zq.glide.fragment.LifecycleCallback
import com.zq.glide.load.IResponseListener
import com.zq.glide.load.LoadData
import com.zq.glide.resource.Key
import com.zq.glide.resource.Value
import com.zq.glide.resource.ValueCallback


class RequestTargetEngine : LifecycleCallback, ValueCallback, MemoryCacheCallback,
    IResponseListener {

    private lateinit var mPath: String

    private lateinit var mContext: Context

    private lateinit var mImageView: ImageView

    private lateinit var mKey: String

    private val mActiveCache: ActiveCache

    private val mMemoryCache: MemoryCache

    private val mDiskLruCacheImpl: DiskLruCacheImpl


    init {
        mActiveCache = ActiveCache(this)

        mMemoryCache = MemoryCache(MAX_SIZE)
        mMemoryCache.memoryCacheCallback = this
        mDiskLruCacheImpl = DiskLruCacheImpl()
    }

    /**
     * 不能在子线程中调用
     */
    fun into(imageView: ImageView) {
        if (!isMainThread()) {
            throw IllegalArgumentException("You must call this method on the main thread")
        }
        var value = cacheAction()
        if (value != null) {

            value.nonUseAction()

            imageView.setImageBitmap(value.bitmap)
        }
    }

    /**
     * 加载资源
     */
    private fun cacheAction(): Value? {
        //从活动缓存中找
        var value = mActiveCache[mKey]
        if (value != null) {
            value.useAction()
            return value
        }
        //从内存缓存中找
        value = mMemoryCache.get(mKey)
        if (value != null) {
            mMemoryCache.shoudonRemove(mKey)
            mActiveCache.put(mKey, value)
            //使用一次  加 1
            value.useAction()
            return value
        }

        //从磁盘缓存中找
        value = mDiskLruCacheImpl[mKey]
        if (value != null) {
            mActiveCache.put(mKey, value)
            //使用一次  加 1
            value.useAction()
            return value
        }

        //从 外部 资源加载 （网络 或者本地加载）。
        value = LoadData().loadResource(mKey, this, mContext)
        return value

    }

    /**
     * 保存到磁盘缓存
     */
    private fun saveCache(key: String, value: Value) {
        value.key = key
        mDiskLruCacheImpl.put(key, value)
    }

    fun loadValueInitAction(path: String, context: Context) {
        this.mKey = Key(path).key
        this.mPath = path
        this.mContext = context
    }

    /**
     * 生命周期回调
     */
    override fun glideStart() {

    }

    /**
     * 生命周期回调
     */
    override fun glideStop() {

    }

    /**
     * 生命周期回调
     */
    override fun glideDestroy() {

    }

    /**
     * 加载外部资源成功
     */
    override fun onSuccess(value: Value) {
        if (value != null) {
            saveCache(mKey, value)
            mImageView.setImageBitmap(value.bitmap)
        }
    }

    /**
     * 加载外部资源失败
     */
    override fun onError(exception: Exception) {

    }

    /**
     * 活动缓存 移除
     */
    override fun valueNonUseListener(key: String, value: Value) {
        //把活动缓存 移除到 内存缓存
        mMemoryCache.put(key, value)
    }


    /**
     * 内存缓存 移除
     */
    override fun entryRemoved(key: String, value: Value) {

    }

    companion object {
        private const val MAX_SIZE = 1024 * 1024 * 60
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }


}