package com.androiddevs.mvvmnewsapp.core

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.androiddevs.mvvmnewsapp.util.TAG

class NewsAppInitializer: Initializer<AppObjectController> {
    override fun create(context: Context): AppObjectController {
        Log.i(TAG, "app initializer create: ")
        return AppObjectController.initLibrary(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}