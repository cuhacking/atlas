package com.cuhacking.atlas

import androidx.test.platform.app.InstrumentationRegistry
import com.cuhacking.atlas.common.FeatureApi
import com.cuhacking.atlas.db.createDatabase
import com.cuhacking.atlas.db.provideDbDriver
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FeatureApiTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val database = provideDbDriver().createDatabase()

    private val sampleData: String =
        """{
                "type": "FeatureCollection",
                "features": [
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Point",
                            "coordinates": [-75.0, 45.0]
                        },
                        "properties":
                        {
                            "id": 10,
                            "name": "sample name",
                            "type": "sample type"
                            
                        }
                    }
                ]
            }""".replace(Regex("[\\s\n]"), "")

    private var client = HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        engine {
            addHandler {
                val header = buildHeaders {
                    append("Content-Type", ContentType.Application.Json.toString())
                    append("Last-Modified", "2020-11-15T23:55:16.626Z")
                }
                respond(sampleData, HttpStatusCode.OK, header)
            }
        }
    }
    private val api = FeatureApi(database, client)

    @Before
    fun before() {
        api.dataCache.appContext = context
        database.featureQueries.run {
            deleteAll()
            clearFts()
        }
    }

    @Test
     fun writeDataWhenCacheIsOldOrEmpty(): Unit = runBlocking {
        api.getAndStoreFeatures()
        assertEquals(sampleData, api.dataCache.readData())
    }

    @Test fun noUpdateWhenServerDataIsTheSame(): Unit = runBlocking {
        api.dataCache.writeData("changed for testing")
        api.getAndStoreFeatures()
        assertEquals("changed for testing", api.dataCache.readData())
    }
}
