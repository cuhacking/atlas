package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Feature as DbFeature
import com.cuhacking.atlas.db.Database
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

class FeatureApi(
    private val database: Database,
    private val client: HttpClient,
    private val dispatchers: CoroutineDispatchers = CoroutineDispatchers
) {
    val dataCache = DataCache(dispatchers)

    private fun Feature.toDbFeature() = DbFeature(
        properties["id"].toString().toLong(),
        properties["name"].toString().replace("\"", ""),
        null,
        properties["type"].toString().replace("\"", ""),
        properties["building"].toString().replace("\"", "").takeIf { it != "null" },
        properties["floor"].toString().replace("\"", "").takeIf { it != "null" },
        properties["name"].toString().replace("\"", ""),
        this)

    @Suppress("TooGenericExceptionCaught")
    suspend fun getAndStoreFeatures() = withContext(dispatchers.io) {
        try {
            val response = client.get<FeatureCollection>(AtlasConfig.SERVER_URL)
            val features: List<Feature> = response.features

            features.forEach {
                database.featureQueries.insertFeature(it.toDbFeature())
            }

            if (updateCache(dataCache.lastModified)) {
                dataCache.writeData(response.toString())
            }
        } catch (exception: Exception) {
            print("Error retrieving data from server $exception")
        }
    }

    private suspend fun updateCache(cacheLastModified: Instant?) = withContext(dispatchers.io) {
        val header = client.head<HttpResponse>(AtlasConfig.SERVER_URL).headers
        val serverLastModified = header["Last-Modified"]?.toInstant()

        if (cacheLastModified == null || serverLastModified == null || serverLastModified > cacheLastModified) {
            return@withContext true
        }
        return@withContext false
    }
}
