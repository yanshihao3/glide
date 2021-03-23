package com.zq.glide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.zq.glide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val imageView by lazy {
        findViewById<ImageView>(R.id.image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        Glide.with(this).load("")
            .into(imageView)

        activityMainBinding.executePendingBindings()
    }
}