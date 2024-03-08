package com.example.humaranews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.humaranews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val items = fetchData()
        val adapter: NewsListAdapter = NewsListAdapter(items, this)
        binding.recyclerViewList.adapter = adapter
    }

    private fun fetchData(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until 100) {
            list.add("Item $i")
        }
        return list
    }

    override fun onNewsItemClicked(item: String) {
        Toast.makeText(this, "Clicked: $item", Toast.LENGTH_SHORT).show()
    }
}