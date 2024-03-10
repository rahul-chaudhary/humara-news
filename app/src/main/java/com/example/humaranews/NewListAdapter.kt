package com.example.humaranews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsListAdapter(
    private val listener: NewsItemListener
) : RecyclerView.Adapter<NewsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.authorView.text = currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.newsImageView)
        holder.itemView.setOnClickListener {
            listener.onNewsItemClicked(items[holder.adapterPosition])
        }

    }

    fun updateNews(updatedItems: ArrayList<News>) {
        items.clear()
        items.addAll(updatedItems)

        notifyDataSetChanged()
    }

}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.newsTitle)
    val newsImageView: ImageView = itemView.findViewById(R.id.newsImage)
    val authorView: TextView = itemView.findViewById(R.id.newsAuthor)
}

interface NewsItemListener {
    fun onNewsItemClicked(item: News)
}