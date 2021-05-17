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
val sharedDatabase = SharedDatabase(::provideDbDriver)

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
fun populateDatabase(httpClient: HttpClient, dataCache: DataCache, sharedDatabase: SharedDatabase) {
    CoroutineScope(CoroutineDispatchers.io).launch {
        sharedDatabase(CoroutineDispatchers.io) { database ->
            FeatureApi(database, httpClient, dataCache, CoroutineDispatchers).getAndStoreFeatures()
        }
    }
}
