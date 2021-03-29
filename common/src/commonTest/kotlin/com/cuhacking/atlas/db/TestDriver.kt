package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver

expect suspend fun createTestDbDriver(): SqlDriver
