package com.cuhacking.atlas

import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.DataCache
import com.cuhacking.atlas.common.FeatureApi
import com.cuhacking.atlas.db.*
import com.cuhacking.atlas.util.runDbTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlin.coroutines.coroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MaxLineLength", "MaximumLineLength")
class FeatureApiTest : TestWithDatabase() {
    private val dispatchers = CoroutineDispatchers
    private val dataCache = DataCache(dispatchers)
    private val sampleFeatureCollection: String =
        """{
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Point",
                            "coordinates": [-75, 45]
                        },
                        "properties":
                        {
                            "id": 1,
                            "name": "sample name",
                            "type": "sample type"
                            
                        }
                    },
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Point",
                            "coordinates": [-75, 45]
                        },
                        "properties":
                        {
                            "id": 2,
                            "name": "sample name",
                            "type": "sample type",
                            "building": "building",
                            "floor": "floor"
                        }
                    }
                ]
            }"""
    private val feature1Json = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{"id":1,"name":"sample name","type":"sample type"}}""")
    private val feature1 = Feature(1, "sample name", null, "sample type", null, null, "sample name", feature1Json)
    private val feature2Json = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{"id":2,"name":"sample name","type":"sample type","building":"building","floor":"floor"}}""")
    private val feature2 = Feature(2, "sample name", null, "sample type", "building", "floor", "sample name", feature2Json)
    private val featureList = mutableListOf(feature1, feature2)

    private val client = HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        engine {
            addHandler {
                respond(sampleFeatureCollection, HttpStatusCode.OK, headersOf("Content-Type", ContentType.Application.Json.toString()))
            }
        }
    }
    private val badClient = HttpClient(MockEngine) {
        engine {
            addHandler {
                respond("Error", HttpStatusCode.BadRequest)
            }
        }
    }

    override suspend fun setupDatabase() {
        super.setupDatabase()
        withDatabase(coroutineContext) { database ->
            database.featureQueries.run {
                deleteAll()
                clearFts()
            }
        }
    }

    @Test
    fun checkDataDownloadAndInsertion() = runDbTest { database ->
        FeatureApi(database, client, dataCache).getAndStoreFeatures()
        assertEquals(featureList, database.featureQueries.getAll().executeAsList())
    }

    @Test
    fun checkNetworkErrorHandling() = runDbTest { database ->
        FeatureApi(database, badClient, dataCache).getAndStoreFeatures()
    }
}
