package com.cuhacking.atlas.common

import com.soywiz.klock.DateTime
import kotlinx.coroutines.withContext
import platform.Foundation.*

@Suppress("MaxLineLength")
actual class DataCache actual constructor(private val dispatchers: CoroutineDispatchers) {
    actual var lastModified: DateTime? = null
    private val cacheDirectory = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true).first()
    private val file = (cacheDirectory as NSString).stringByAppendingPathComponent("mapdata.json")

    @Suppress("CAST_NEVER_SUCCEEDS")
    actual suspend fun writeData(data: String) = withContext(dispatchers.io) {
        lastModified = DateTime.now()
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
