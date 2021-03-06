package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.BuildConfig
import com.androiddevs.mvvmnewsapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPINetworkService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "in",
        @Query("page") pageNumber: Int = Constants.DEFAULT_PAGE_NUMBER,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = Constants.DEFAULT_PAGE_NUMBER,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ) : Response<NewsResponse>
}