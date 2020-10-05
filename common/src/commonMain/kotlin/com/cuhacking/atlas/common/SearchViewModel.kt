package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Database
import com.cuhacking.atlas.db.Feature_fts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class SearchViewModel(private val database: Database, private val dispatchers: CoroutineDispatchers) {

    private val _searchResults: MutableStateFlow<List<SearchResult>> = MutableStateFlow(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    suspend fun getSearchResults(query: String) = withContext(dispatchers.io) {
        val results: List<Feature_fts> = database.featureQueries.search(query).executeAsList()

        val searchResultItems = results.map {
            // Sample Search Result data (could be changed later)
            SearchResult(it.common_name, it.secondary_name.toString() + ", " + it.type)
        }
        _searchResults.value = searchResultItems
    }
}

