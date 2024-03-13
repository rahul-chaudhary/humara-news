package com.example.humaranews

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.humaranews.databinding.ActivityHomeScreenBinding
import com.example.humaranews.databinding.ActivityMainBinding

class HomeScreen: AppCompatActivity(), NewsItemListener {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
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
        val category = "technology"
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=$category"
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
        // get the current toolbar background color (this might work differently in your app)
        @ColorInt val colorPrimaryLight =
            ContextCompat.getColor(this@HomeScreen, R.color.holo_red_dark)
        @ColorInt val colorPrimaryDark =
            ContextCompat.getColor(this@HomeScreen, R.color.darker_gray)

        val intent = CustomTabsIntent.Builder() // set the default color scheme
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(colorPrimaryLight)
                    .build()
            ) // set the alternative dark color scheme
            .setColorSchemeParams(
                CustomTabsIntent.COLOR_SCHEME_DARK, CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(colorPrimaryDark)
                    .build()
            )
//            .setStartAnimations(this@MainActivity, R.anim.bounce_interpolator, R.anim.accelerate_decelerate_interpolator)
            .setStartAnimations(this@HomeScreen, R.anim.slide_in_left, R.anim.slide_out_right)
            .setExitAnimations(this@HomeScreen, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .build()
        val url = item.url
//        val intent = CustomTabsIntent.Builder()
//            .build()
        intent.launchUrl(this@HomeScreen, Uri.parse(url))
    }

}