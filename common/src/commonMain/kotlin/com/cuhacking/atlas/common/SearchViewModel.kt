package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Database
import com.cuhacking.atlas.db.Feature_fts
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(private val database: Database) {

    private val _searchResults: MutableStateFlow<List<SearchResult>> = MutableStateFlow(listOf())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    suspend fun getSearchResults(query: String) = coroutineScope {
        val results: List<Feature_fts> = database.featureQueries.search(query).executeAsList()

        val searchResultItems = results.map {
            // Sample Search Result data (could be changed later)
            SearchResult(it.common_name, it.secondary_name.toString() + ", " + it.type)
        }
        _searchResults.value = searchResultItems
    }
}
