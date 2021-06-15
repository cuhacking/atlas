package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.appContext
import com.soywiz.klock.DateTime
import kotlinx.coroutines.withContext
import java.io.File

actual class DataCache actual constructor(private val dispatchers: CoroutineDispatchers) {
    actual var lastModified: DateTime? = null

    actual suspend fun writeData(data: String) = withContext(dispatchers.io) {
        lastModified = DateTime.now()
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
