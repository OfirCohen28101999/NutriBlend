package com.example.nutriblend.base

import android.app.Application
import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File


class MyApplication: Application() {
    object Globals {
        var appContext:  Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
        initPicasso()
    }

    private fun initPicasso() {
        val cacheDir = File(applicationContext.cacheDir, "picasso-cache")
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB

        val okHttpClient = OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize))
            .build()

        val picasso = Picasso.Builder(applicationContext)
            .downloader(OkHttp3Downloader(okHttpClient))
            .loggingEnabled(true)
            .build()

        Picasso.setSingletonInstance(picasso)
    }
}