package com.cuhacking.atlas.common

import com.soywiz.klock.DateTime

actual class DataCache actual constructor(private val dispatchers: CoroutineDispatchers) {
    actual var lastModified: DateTime? = null
    private var jsonData = ""

    actual suspend fun writeData(data: String) {
        lastModified = DateTime.now()
        jsonData = data
    }

    actual suspend fun readData(): String = jsonData
}
