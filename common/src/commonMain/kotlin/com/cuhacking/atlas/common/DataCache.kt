package com.cuhacking.atlas.common

import kotlinx.datetime.Instant

expect class DataCache constructor(dispatchers: CoroutineDispatchers = CoroutineDispatchers) {
    var lastModified: Instant?
        private set
    suspend fun writeData(data: String)
    suspend fun readData(): String
}
