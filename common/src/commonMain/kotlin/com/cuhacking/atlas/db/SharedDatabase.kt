package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Allows for asynchronous creation of an [AtlasDatabase] instance that guarantees the database
 * is initialized before use.
 *
 * To use, construct an instance of this class and then call the [invoke] method.
 * ```kotlin
 * val withDatabase: SharedDatabase() = SharedDatabase(provideDbDriver)
 *
 * withDatabase(Dispatchers.IO) { database ->
 *     // Query database here
 * }
 * ```
 *
 * @property driverProvider A coroutine that creates the [SqlDriver] to use
 */
class SharedDatabase(private val driverProvider: suspend () -> SqlDriver) {
    private val mutex = Mutex()
    private var database: AtlasDatabase? = null
    private var driver: SqlDriver? = null

    /**
     * Initializes the database instance ahead of time
     */
    suspend fun initDatabase() {
        mutex.withLock {
            if (database == null) {
                driver = driverProvider()
                database = driver!!.createDatabase()
            }
        }
    }

    /**
     * Ensures that the database instance has been created, and then calls [block] with the database
     * instance passed in.
     *
     * If you want to initialize the database ahead of time, use [initDatabase].
     *
     * @param context The [CoroutineContext] to run the [block] on.
     * @param block The code to run with the database instance.
     */
    suspend operator fun invoke(context: CoroutineContext, block: suspend (AtlasDatabase) -> Unit) {
        withContext(context) {
            initDatabase()
            block(database!!)
        }
    }

    fun close() {
        driver?.close()
    }

    private fun SqlDriver.createDatabase(): AtlasDatabase {
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
            Feature(
                1,
                "River Building",
                "Richcraft Hall",
                "Building",
                null,
                null,
                null,
                sampleFeature
            )
        val feature2 =
            Feature(2, "RB 2311", null, "room", "River Building", "RB2", null, sampleFeature)

        @Suppress("MagicNumber")
        val feature3 =
            Feature(
                3,
                "HS 1301B",
                null,
                "room",
                "Health Sciences Building",
                "HS1",
                null,
                sampleFeature
            )
        database.featureQueries.insertFeature(feature1)
        database.featureQueries.insertFeature(feature2)
        database.featureQueries.insertFeature(feature3)

        return database
    }
}

expect suspend fun provideDbDriver(): SqlDriver
