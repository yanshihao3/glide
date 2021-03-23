package com.zq.glide.fragment

import androidx.fragment.app.Fragment

/**
 * @program: glide
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-03-23 13:44
 **/
class FragmentActivityFragmentManager : Fragment {

    private val lifecycleCallback: LifecycleCallback

    constructor(lifecycleCallback: LifecycleCallback) {
        this.lifecycleCallback = lifecycleCallback
    }

    override fun onStart() {
        super.onStart()
        lifecycleCallback.glideStart()
    }

    override fun onStop() {
        super.onStop()
        lifecycleCallback.glideStop()

    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleCallback.glideDestroy()
    }
}