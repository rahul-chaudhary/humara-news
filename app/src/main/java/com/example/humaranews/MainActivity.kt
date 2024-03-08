package com.example.humaranews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.humaranews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerViewList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
         fetchData()
        mAdapter = NewsListAdapter(this)
        val adapter: NewsListAdapter = NewsListAdapter( this)
        binding.recyclerViewList.adapter = adapter
    }

    private fun fetchData(){
        val apiKey = "de85900fb6ad4597a302037d0e4e9018"
        val url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=$apiKey"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
             mAdapter.updateNews(newsArray)
            },
            { error ->
                Log.d("Response log", "Error is : $error")
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onNewsItemClicked(item: News) {

    }

}