package com.zq.glide

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zq.glide.fragment.FragmentActivityFragmentManager

/**
 * @program: glide
 *
 * @description:生命周期管理
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 13:41
 **/
class RequestManager {
    private val requestTargetEngine: RequestTargetEngine = RequestTargetEngine()
    private val mHandler: Handler = Handler(Looper.getMainLooper()) {
        false
    }
    private val mContext: Context

    constructor(fragmentActivity: FragmentActivity) {
        mContext = fragmentActivity
        val supportFragmentManager = fragmentActivity.supportFragmentManager
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME)
        if (fragment == null) {
            fragment = FragmentActivityFragmentManager(requestTargetEngine)
        }
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(fragment, FRAGMENT_ACTIVITY_NAME).commitAllowingStateLoss()
        mHandler.sendEmptyMessage(1)
    }

    constructor(context: Context) {
        mContext = context
    }

    fun load(path: String): RequestTargetEngine {
        mHandler.removeMessages(1)
        requestTargetEngine.loadValueInitAction(path,mContext)
        return requestTargetEngine
    }


    companion object {
        private const val FRAGMENT_ACTIVITY_NAME = "FRAGMENT_ACTIVITY_NAME"
    }
}

