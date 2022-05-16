package com.androiddevs.mvvmnewsapp.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>: Resource<T>()
}

class DatabaseTask(value: String) {
    companion object {
        const val IDLE = "Idle"
        const val SUCCESS_ADD = "Article saved successfully"
        const val SUCCESS_DELETE = "Article deleted successfully"
        const val FAILED_ADD = "Failed to add article"
        const val FAILED_DELETE = "Failed to delete article"
    }
}