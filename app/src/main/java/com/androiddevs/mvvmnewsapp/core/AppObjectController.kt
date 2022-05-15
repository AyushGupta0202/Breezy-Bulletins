package com.androiddevs.mvvmnewsapp.core

import android.content.Context
import com.androiddevs.mvvmnewsapp.BuildConfig
import com.androiddevs.mvvmnewsapp.api.NewsAPINetworkService
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

class AppObjectController {
    companion object {
        private const val TIMEOUT: Long = 10000L

        @JvmStatic
        private var INSTANCE: AppObjectController = AppObjectController()

        @JvmStatic
        lateinit var newsApplication: NewsApplication
            private set

        @JvmStatic
        lateinit var appDatabase: ArticleDatabase
            private set

        @JvmStatic
        lateinit var retrofit: Retrofit
            private set

        @JvmStatic
        lateinit var newsNetworkService: NewsAPINetworkService
            private set

        fun initLibrary(context: Context): AppObjectController {
            CoroutineScope(Dispatchers.IO).launch {
                newsApplication = context as NewsApplication
                appDatabase = ArticleDatabase.invoke(context)
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(OkHttpProfilerInterceptor())
                    .addNetworkInterceptor(getStethoInterceptor())
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()


                newsNetworkService = retrofit.create(NewsAPINetworkService::class.java)
            }
            return INSTANCE
        }

        fun init(context: NewsApplication) {
            newsApplication = context
            CoroutineScope(Dispatchers.IO).launch {
                initStethoLibrary(context)
            }
        }
    }
}

fun initStethoLibrary(context: Context) {
    val cls = Class.forName("com.facebook.stetho.Stetho")
    val m: Method = cls.getMethod("initializeWithDefaults", Context::class.java)
    m.invoke(null, context)
}

fun getStethoInterceptor(): Interceptor {
    val clazz = Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor")
    val ctor: Constructor<*> = clazz.getConstructor()
    return ctor.newInstance() as Interceptor
}


