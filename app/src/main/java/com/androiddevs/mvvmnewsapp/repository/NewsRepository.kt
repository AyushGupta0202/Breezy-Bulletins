package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.api.Article
import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.core.AppObjectController

class NewsRepository {

    private val db = AppObjectController.appDatabase
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        AppObjectController.newsNetworkService.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        AppObjectController.newsNetworkService.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.articleDao().upsert(article)

    fun getSavedNews() = db.articleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)
}
