package com.cuhacking.atlas.common.di

import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.DataCache
import com.cuhacking.atlas.common.FeatureApi
import com.cuhacking.atlas.common.data.DataRepository
import com.cuhacking.atlas.db.SharedDatabase
import com.cuhacking.atlas.db.provideDbDriver
import io.ktor.client.*
import kotlin.js.JsExport


val dataCache = DataCache()

val httpClient = HttpClient()

val sharedDatabase = SharedDatabase(::provideDbDriver)

val featureApi = FeatureApi(sharedDatabase, httpClient, dataCache, CoroutineDispatchers)

@JsExport
val dataRepository = DataRepository(CoroutineDispatchers, dataCache, featureApi)