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
        return AtlasDatabase(
                driver = this,
                featureAdapter = Feature.Adapter(
                        jsonAdapter = GeoJsonAdapter
                )
        )
    }
}

expect suspend fun provideDbDriver(): SqlDriver
