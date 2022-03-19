package com.androiddevs.mvvmnewsapp.articles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.androiddevs.mvvmnewsapp.NewsActivity
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.breakingNews.viewmodel.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article){
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as NewsActivity).viewModel
    }
}