package com.cuhacking.atlas.util

import com.cuhacking.atlas.db.AtlasDatabase
import com.cuhacking.atlas.db.TestWithDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

actual fun TestWithDatabase.runDbTest(block: suspend (AtlasDatabase) -> Unit): dynamic =
    GlobalScope.promise {
        setupDatabase()
        withDatabase(coroutineContext, block)
        cleanupDatabase()
    }
