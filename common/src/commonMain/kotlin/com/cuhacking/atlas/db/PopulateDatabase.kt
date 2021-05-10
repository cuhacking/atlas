package com.cuhacking.atlas.db

import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.DataCache
import com.cuhacking.atlas.common.FeatureApi
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.js.JsExport

@JsExport
val dataCache = DataCache()

@JsExport
val httpClient = HttpClient()

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
fun populateDatabase(httpClient: HttpClient, dataCache: DataCache) {
    CoroutineScope(CoroutineDispatchers.io).launch {
        SharedDatabase(::provideDbDriver)(CoroutineDispatchers.io) { database ->
            FeatureApi(database, httpClient, dataCache, CoroutineDispatchers).getAndStoreFeatures()
        }
    }
}
