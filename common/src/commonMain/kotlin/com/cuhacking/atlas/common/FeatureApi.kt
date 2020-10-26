package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Feature as DbFeature
import com.cuhacking.atlas.db.Database
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.withContext

class FeatureApi(private val database: Database, private val client: HttpClient) {
    private val dispatchers = CoroutineDispatchers
    private val dataCache = DataCache(dispatchers)

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

            dataCache.writeData(response.toString())
        } catch (exception: Exception) {
            print("Error retrieving data from server $exception")
        }
    }
}
