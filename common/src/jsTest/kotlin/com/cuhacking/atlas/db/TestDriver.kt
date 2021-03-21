package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await

actual suspend fun createTestDbDriver(): SqlDriver = initSqlDriver(AtlasDatabase.Schema).await()
