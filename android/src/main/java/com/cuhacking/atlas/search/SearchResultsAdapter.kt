package com.cuhacking.atlas.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.atlas.R

class SearchResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val result: TextView = view.findViewById(R.id.result)
    private val description: TextView = view.findViewById(R.id.description)

    fun bind(searchResult: SearchResult) {
        result.text = searchResult.name
        description.text = searchResult.description
    }
}

class SearchResultsAdapter(
    private val clickHandler: (SearchResult) -> Unit
) : ListAdapter<SearchResult, SearchResultsViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return SearchResultsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}
