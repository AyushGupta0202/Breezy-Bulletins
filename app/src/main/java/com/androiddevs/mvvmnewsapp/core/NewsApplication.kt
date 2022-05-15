package com.androiddevs.mvvmnewsapp.core

import android.app.Application
import android.util.Log
import com.androiddevs.mvvmnewsapp.util.TAG

class NewsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "application onCreate: ")
        AppObjectController.init(this)
    }
}