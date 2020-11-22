package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Feature as DbFeature
import com.cuhacking.atlas.db.Database
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

@Suppress("MaxLineLength", "MaximumLineLength")
class FeatureApi(
    private val database: Database,
    private val client: HttpClient,
    private val dataCache: DataCache,
    private val dispatchers: CoroutineDispatchers = CoroutineDispatchers
) {
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
            if (updateCache(dataCache.lastModified)) {
                val response = client.get<String>(AtlasConfig.SERVER_URL)

                val features: List<Feature> = response.toFeatureCollection().features

                features.forEach {
                    database.featureQueries.insertFeature(it.toDbFeature())
                }

                dataCache.writeData(response)
            }
        } catch (exception: Exception) {
            print("Error retrieving data from server $exception")
        }
    }

    private suspend fun updateCache(cacheLastModified: Instant?) = withContext(dispatchers.io) {
        val header = client.head<HttpResponse>(AtlasConfig.SERVER_URL).headers
        val serverLastModified = header["Last-Modified"]?.toInstant()
        return@withContext cacheLastModified == null || serverLastModified == null || serverLastModified > cacheLastModified
    }
}
