package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.SharedDatabase
import com.cuhacking.atlas.util.listAdapt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.js.JsExport

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
class SearchViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val scope: CoroutineScope,
    private val withDatabase: SharedDatabase
) {
    private val _searchResults = MutableStateFlow(emptyList<SearchResult>())
    val searchResults = _searchResults.listAdapt(scope)

    fun getSearchResults(query: String) {
        scope.launch {
            withDatabase(dispatchers.io) { database ->
                val results = database.featureQueries.search(query).executeAsList()
                    .map {
                        // Sample Search Result data (could be changed later)
                        SearchResult(it.common_name, it.secondary_name.toString() + ", " + it.type)
                    }
                _searchResults.value = results
            }
        }
    }
}
