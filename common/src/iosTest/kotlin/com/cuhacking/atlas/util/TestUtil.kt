package com.cuhacking.atlas.util

import com.cuhacking.atlas.db.AtlasDatabase
import com.cuhacking.atlas.db.TestWithDatabase
import kotlinx.coroutines.runBlocking

actual fun TestWithDatabase.runDbTest(block: suspend (AtlasDatabase) -> Unit) {
    runBlocking {
        setupDatabase()
        withDatabase(coroutineContext, block)
        cleanupDatabase()
    }
}
