package com.zq.glide.load

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.zq.glide.resource.Value
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


/**
 * @program: glide
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 15:16
 **/
class LoadData : ILoadData, Runnable {
    /**
     * 加载 网络图片 或者 SD 本地图片
     */
    private lateinit var path: String

    private lateinit var context: Context

    private lateinit var responseListener: IResponseListener
    private val handler = Handler(Looper.getMainLooper())
    override fun loadResource(
        path: String,
        responseListener: IResponseListener,
        context: Context
    ): Value? {
        this.context = context
        this.path = path
        this.responseListener = responseListener

        val uri = Uri.parse(path)
        //网络图片
        if ("http".equals(uri.scheme, true) || "https".equals(uri.scheme, true)) {

            val threadPool = Executors.newCachedThreadPool()
            threadPool.execute(this)
        } else { //本地图片

        }
        return null
    }

    override fun run() {
        var inputStream: InputStream? = null
        var httpURLConnection: HttpURLConnection? = null // HttpURLConnection内部已经是Okhttp，因为太高效了
        try {
            val url = URL(path)
            httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.connectTimeout = 5000
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                // TODO 同学们注意：我们没有写这个代码： Bitmap 做缩放，做比例，做压缩，.....

                // 成功 切换主线程
                handler.post {
                    val value: Value = Value.instance
                    value.bitmap = bitmap
                    // 回调成功
                    responseListener.onSuccess(value)
                }
            } else {
                // 失败 切换主线程
                handler.post { // 回调失败
                    responseListener.onError(IllegalStateException("请求失败，请求码：$responseCode"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            httpURLConnection?.disconnect()
        }
    }
}