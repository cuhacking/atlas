package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.*
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.parse
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import com.cuhacking.atlas.db.Feature as DbFeature

@Suppress("MaxLineLength", "MaximumLineLength")
class FeatureApi(
    private val withDatabase: SharedDatabase,
    private val client: HttpClient,
    private val dataCache: DataCache,
    private val dispatchers: CoroutineDispatchers = CoroutineDispatchers
) {
    private fun Feature.toDbFeature(): DbFeature {
        return DbFeature(
            properties["fid"]?.jsonPrimitive!!.long,
            properties["roomId"]?.jsonPrimitive!!.content,
            properties["roomName"]?.jsonPrimitive?.contentOrNull,
            properties["type"]?.jsonPrimitive!!.content,
            properties["building"]?.jsonPrimitive?.contentOrNull,
            properties["floor"]?.jsonPrimitive?.contentOrNull,
            null,
            this)
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun getAndStoreFeatures() = withDatabase(dispatchers.io) { database ->
        try {
            if (updateCache(dataCache.lastModified)) {
                database.featureQueries.run {
                    deleteAll()
                    clearFts()
                }
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

    private suspend fun updateCache(cacheLastModified: DateTime?) = withContext(dispatchers.io) {
        val header = client.head<HttpResponse>(AtlasConfig.SERVER_URL).headers
        val dateFormat = DateFormat("EEE, dd MMM yyyy HH:mm:ss z")
        val serverLastModified = header["Last-Modified"]?.let { dateFormat.parse(it) }?.local
        return@withContext cacheLastModified == null || serverLastModified == null || serverLastModified > cacheLastModified
    }
}
