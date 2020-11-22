package com.cuhacking.atlas.common

import android.content.Context
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.io.File

actual class DataCache actual constructor(private val dispatchers: CoroutineDispatchers) {
    lateinit var appContext: Context
    actual var lastModified: Instant? = null

    actual suspend fun writeData(data: String) = withContext(dispatchers.io) {
        lastModified = Clock.System.now()
        val file = File(appContext.cacheDir, "mapdata.json")
        file.bufferedWriter().use {
            it.write(data)
        }
    }

    actual suspend fun readData(): String = withContext(dispatchers.io) {
        val file = File(appContext.cacheDir, "mapdata.json")
        return@withContext file.bufferedReader().use {
            it.readText()
        }
    }
}
