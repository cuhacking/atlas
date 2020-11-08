package com.cuhacking.atlas.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

actual class DataCache actual constructor(private val dispatchers: CoroutineDispatchers) {
    actual var lastModified: Instant? = null
    private var jsonData = ""

    actual suspend fun writeData(data: String) {
        lastModified = Clock.System.now()
        jsonData = data
    }

    actual suspend fun readData(): String = jsonData
}
