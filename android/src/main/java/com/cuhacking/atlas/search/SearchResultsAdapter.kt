package com.cuhacking.atlas.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cuhacking.atlas.common.SearchResult
import com.cuhacking.atlas.databinding.ItemSearchResultBinding

class SearchResultsViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(searchResult: SearchResult) {
        binding.result.text = searchResult.name
        binding.description.text = searchResult.description
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
        val inflater = LayoutInflater.from(parent.context)
        return SearchResultsViewHolder(ItemSearchResultBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}
