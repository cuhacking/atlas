package com.cuhacking.atlas.db

import com.cuhacking.atlas.util.runDbTest
import kotlin.coroutines.coroutineContext
import kotlin.test.Test
import kotlin.test.assertTrue

@Suppress("MaxLineLength")
class DatabaseTest : TestWithDatabase() {
    private val sampleFeature =
        GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point","coordinates":[-75.0,45.0]},"properties":{}}""")
    private val feature1 =
        Feature(1, "River Building", "Richcraft Hall", "Building", null, null, null, sampleFeature)
    private val feature2 =
        Feature(2, "RB 2311", null, "room", "River Building", "RB2", null, sampleFeature)
    private val feature3 =
        Feature(3, "HS 1301B", null, "room", "Health Sciences Building", "HS1", null, sampleFeature)

    private fun Feature.toFTS() =
        (Feature_fts(common_name, secondary_name, type, building, floor, search_tags))

    override suspend fun setupDatabase() {
        super.setupDatabase()
        withDatabase(coroutineContext) { database ->
            database.featureQueries.run {
                deleteAll()
                clearFts()
                insertFeature(feature1)
                insertFeature(feature2)
            }
        }
    }

    @Test
    fun checkInsertionMirroredInFts() = runDbTest { database ->
        database.featureQueries.insertFeature(feature3)

        assertTrue(
            database.featureQueries.search("HS 1301B").executeAsList()
                .contains(feature3.toFTS())
        )
    }

    @Test
    fun checkSearchQuery() = runDbTest { database ->
        assertTrue(
            database.featureQueries.search("river").executeAsList().containsAll(
                listOf(feature1.toFTS(), feature2.toFTS())
            )
        )
    }

    @Test
    fun checkClearingFts() = runDbTest { database ->
        database.featureQueries.clearFts()
        assertTrue(database.featureQueries.search("river").executeAsOneOrNull() == null)
    }
}
