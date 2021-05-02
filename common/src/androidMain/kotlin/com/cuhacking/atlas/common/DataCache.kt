package com.cuhacking.atlas.common

import android.content.Context
import com.soywiz.klock.DateTime
import kotlinx.coroutines.withContext
import java.io.File

actual class DataCache actual constructor(private val dispatchers: CoroutineDispatchers) {
    lateinit var appContext: Context
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
