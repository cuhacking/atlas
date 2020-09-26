package com.cuhacking.atlas.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cuhacking.atlas.db.Feature_fts
import com.cuhacking.atlas.db.database

class SearchViewModel : ViewModel() {

    private val _searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

        fun getSearchResults(query: String) {
        val results: List<Feature_fts> = database.featureQueries.search(query).executeAsList()

        val searchResultItems = results.map {
            // Sample Search Result data (could be changed later)
            SearchResult(it.common_name, it.secondary_name.toString() + ", " + it.type)
        }
        _searchResults.value = searchResultItems
    }
}
