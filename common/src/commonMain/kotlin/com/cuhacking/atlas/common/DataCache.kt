package com.cuhacking.atlas.common

import com.soywiz.klock.DateTime

expect class DataCache constructor(dispatchers: CoroutineDispatchers = CoroutineDispatchers) {
    var lastModified: DateTime?
        private set
    suspend fun writeData(data: String)
    suspend fun readData(): String
}
