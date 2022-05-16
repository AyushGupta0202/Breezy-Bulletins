package com.androiddevs.mvvmnewsapp.commonviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.api.Article
import com.androiddevs.mvvmnewsapp.api.NewsResponse
import com.androiddevs.mvvmnewsapp.core.AppObjectController
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.DatabaseTask
import com.androiddevs.mvvmnewsapp.util.Resource
import com.androiddevs.mvvmnewsapp.util.TAG
import com.androiddevs.mvvmnewsapp.util.Utils
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel: ViewModel() {

    private val repository = NewsRepository()
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null
    val databaseTask = MutableLiveData<String>()

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        try {
            if (Utils.hasInternetConnection()) {
                breakingNews.postValue(Resource.Loading())
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error(AppObjectController.newsApplication.getString(R.string.no_internet)))
            }
        } catch (ex: Exception) {
            breakingNews.postValue(Resource.Error(ex.localizedMessage ?: ""))
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        try {
            if (Utils.hasInternetConnection()) {
                searchNews.postValue(Resource.Loading())
                val response = repository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error(AppObjectController.newsApplication.getString(R.string.no_internet)))
            }
        } catch (ex: Exception) {
            searchNews.postValue(Resource.Error(ex.localizedMessage ?: ""))
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) {
        Log.i(TAG, "saveArticle: article: $article")
        viewModelScope.launch {
            val task = repository.upsert(article)
            databaseTask.postValue(if (task != -1L) DatabaseTask.SUCCESS_ADD else DatabaseTask.FAILED_ADD)
        }
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun changeDatabaseToIdle() {
        databaseTask.postValue(DatabaseTask.IDLE)
    }
}