package com.cuhacking.atlas.common

import kotlinx.datetime.Instant

expect class DataCache() {
    var lastModified: Instant?
    suspend fun writeData(data: String)
    suspend fun readData(): String
}
