package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Feature_fts
import com.cuhacking.atlas.db.withDatabase
import com.cuhacking.atlas.util.adapt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.js.JsExport

@JsExport
class SearchViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val scope: CoroutineScope
) {
    private val _searchResults = MutableStateFlow(emptyList<SearchResult>())
    val searchResults = _searchResults.adapt(scope)

    fun getSearchResults(query: String) {
        scope.launch {
            withDatabase(dispatchers.io) { database ->
                val results: List<Feature_fts> =
                    database.featureQueries.search(query).executeAsList()

                val searchResultItems = results.map {
                    // Sample Search Result data (could be changed later)
                    SearchResult(it.common_name, it.secondary_name.toString() + ", " + it.type)
                }
                _searchResults.value = searchResultItems
            }
        }
    }
}

