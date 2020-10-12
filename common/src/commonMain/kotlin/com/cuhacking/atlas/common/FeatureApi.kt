package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.Feature as DbFeature
import com.cuhacking.atlas.db.Database
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.withContext

class FeatureApi (private val database: Database, private val client: HttpClient) {

    private fun Feature.toDbFeature() = DbFeature(
        properties["id"].toString().toLong(),
        properties["name"].toString().replace("\"", ""),
        null,
        properties["type"].toString().replace("\"", ""),
        properties["building"].toString().replace("\"", ""). takeIf { it != "null" },
        properties["floor"].toString().replace("\"", ""). takeIf { it != "null" },
        properties["name"].toString().replace("\"", ""),
        this)

    suspend fun getAndStoreFeatures() = withContext(CoroutineDispatchers.io) {
        val features : List<Feature> = client.get<String>(AtlasConfig.SERVER_URL).toFeatureCollection().features

        features.forEach {
            database.featureQueries.insertFeature(it.toDbFeature())
        }
    }
}

