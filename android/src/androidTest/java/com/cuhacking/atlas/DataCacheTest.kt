package com.cuhacking.atlas

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.DataCache
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataCacheTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val dispatchers = CoroutineDispatchers
    private val dataCache = DataCache(dispatchers)

    private val sampleData: String =
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
    @Before
    fun before() {
       dataCache.appContext = context
    }

    @Test
    fun writeData() = runBlocking {
        dataCache.writeData(sampleData)
    }

    @Test
    fun readData() = runBlocking {
        val data = dataCache.readData()
        assertEquals(data, sampleData)
    }
}
