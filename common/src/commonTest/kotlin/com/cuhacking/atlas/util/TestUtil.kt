package com.cuhacking.atlas.util

import com.cuhacking.atlas.db.AtlasDatabase
import com.cuhacking.atlas.db.TestWithDatabase

expect fun TestWithDatabase.runDbTest(block: suspend (AtlasDatabase) -> Unit)
