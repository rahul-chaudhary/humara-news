package com.example.humaranews

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
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
//        val adapter = NewsListAdapter( this)
        binding.recyclerViewList.adapter = mAdapter
    }

    private fun fetchData() {
        val apiKey = "de85900fb6ad4597a302037d0e4e9018"
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                if (it.has("status") && it.getString("status") == "ok") {
                    Log.d("Response log", " Response is : $it")
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for (i in 0 until newsJsonArray.length()) {
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
                } else if (it.has("status") && it.getString("status") == "error") {
                    Log.d("Response log", "Error is : ${it.getString("message")}")
                } else Log.d("Response log", "Error is : $it")
            },
            { error ->
                Log.d("Response log", "Error is : $error")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                headers["Authorization"] = "Bearer $apiKey"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onNewsItemClicked(item: News) {
        val url = item.url
        val intent = CustomTabsIntent.Builder()
            .build()
        intent.launchUrl(this@MainActivity, Uri.parse(url))
    }

}