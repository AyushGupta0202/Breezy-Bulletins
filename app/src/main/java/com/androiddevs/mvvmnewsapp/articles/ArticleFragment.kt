package com.androiddevs.mvvmnewsapp.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.commonviewmodel.NewsViewModel
import com.androiddevs.mvvmnewsapp.databinding.FragmentArticleBinding
import com.androiddevs.mvvmnewsapp.util.DatabaseTask
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(){
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
    }
    private val args: ArticleFragmentArgs by navArgs()

    private lateinit var binding: FragmentArticleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        binding.article = args.article!!
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        val article = args.article

        binding.apply {
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }

//            fab.setOnClickListener {
//                viewModel.saveArticle(article)
//            }
        }
    }

    private fun addObservers() {
        viewModel.databaseTask.observe(viewLifecycleOwner) {
            if (it != DatabaseTask.IDLE) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.changeDatabaseToIdle()
            }
        }
    }
}