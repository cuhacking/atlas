package com.cuhacking.atlas.db

open class TestWithDatabase {
    val withDatabase: SharedDatabase = SharedDatabase(::createTestDbDriver)

    open suspend fun setupDatabase() {
        withDatabase.initDatabase()
    }

    open fun cleanupDatabase() {
        withDatabase.close()
    }
}
