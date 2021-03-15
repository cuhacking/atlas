package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await

actual suspend fun provideDbDriver(): SqlDriver {
    return initSqlDriver(AtlasDatabase.Schema).await()
}
