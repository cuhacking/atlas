package com.cuhacking.atlas.common

import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import platform.Foundation.*

actual class DataCache actual constructor(dispatchers: CoroutineDispatchers) {
    actual var lastModified: Instant? = null
    private val cacheDirectory = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true).first()
    private val file = (cacheDirectory as NSString).stringByAppendingPathComponent("mapdata.json")
    private val dispatchers = CoroutineDispatchers

    @Suppress("CAST_NEVER_SUCCEEDS")
    actual suspend fun writeData(data: String) = withContext(dispatchers.io) {
        lastModified = Clock.System.now()
        val writeToFile: Boolean = (data as NSString).writeToFile(file, true, NSUTF8StringEncoding, null)
        if (writeToFile) {
            NSLog("Success writing to file $file")
        } else {
            NSLog("Error writing to file $file")
        }
    }

    actual suspend fun readData(): String = withContext(dispatchers.io) {
        return@withContext NSString.stringWithContentsOfFile(file, NSUTF8StringEncoding, null).toString()
    }
}
