package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual suspend fun provideDbDriver(): SqlDriver {
    return NativeSqliteDriver(AtlasDatabase.Schema, "Feature.db")
}
