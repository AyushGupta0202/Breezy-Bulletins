package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.util.Constants.Companion.BASE_URL
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Constructor
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {
        private const val TIMEOUT: Long = 10000L
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .addInterceptor(OkHttpProfilerInterceptor())
                .addNetworkInterceptor(getStethoInterceptor())
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val newsNetworkService: NewsAPINetworkService by lazy {
            retrofit.create(NewsAPINetworkService::class.java)
        }
    }
}

fun getStethoInterceptor(): Interceptor {
    val clazz = Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor")
    val ctor: Constructor<*> = clazz.getConstructor()
    return ctor.newInstance() as Interceptor
}