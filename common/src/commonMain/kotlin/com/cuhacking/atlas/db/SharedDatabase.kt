package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

private var database: AtlasDatabase? = null
private val mutex = Mutex()

expect suspend fun provideDbDriver(): SqlDriver

fun SqlDriver.createDatabase(): AtlasDatabase {
    // Uncomment the following code and delete the rest. It was used for testing only
//    return Database(
//            driver = this,
//            featureAdapter = Feature.Adapter(
//                    jsonAdapter = GeoJsonAdapter
//            )
//    )
//    Database.Schema.create(this)
    val database = AtlasDatabase(
        driver = this,
        featureAdapter = Feature.Adapter(
            jsonAdapter = GeoJsonAdapter
        )
    )
    database.featureQueries.deleteAll()
    database.featureQueries.clearFts()

    val sampleFeature = GeoJsonAdapter.decode(
        """{"type":"Feature","geometry":{"type":"Point",
        |"coordinates":[-75.0,45.0]},"properties":{}}""".trimMargin()
    )
    val feature1 =
        Feature(1, "River Building", "Richcraft Hall", "Building", null, null, null, sampleFeature)
    val feature2 = Feature(2, "RB 2311", null, "room", "River Building", "RB2", null, sampleFeature)

    @Suppress("MagicNumber")
    val feature3 =
        Feature(3, "HS 1301B", null, "room", "Health Sciences Building", "HS1", null, sampleFeature)
    database.featureQueries.insertFeature(feature1)
    database.featureQueries.insertFeature(feature2)
    database.featureQueries.insertFeature(feature3)

    return database
}

/**
 * Ensures the construction of a [AtlasDatabase] object is complete before the execution of [block].
 * The constructed database is passed into the block for use.
 */
suspend fun withDatabase(context: CoroutineContext, block: suspend (AtlasDatabase) -> Unit) =
    withContext(context) {
        mutex.withLock {
            if (database == null) {
                database = provideDbDriver().createDatabase()
            }
        }
        block(database!!)
    }
