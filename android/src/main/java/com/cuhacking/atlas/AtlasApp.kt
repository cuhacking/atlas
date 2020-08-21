package com.cuhacking.atlas

import android.app.Application
import com.cuhacking.atlas.db.appContext
import com.cuhacking.atlas.db.createDatabase
import com.cuhacking.atlas.db.database
import com.cuhacking.atlas.db.provideDbDriver


class AtlasApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        database = provideDbDriver().createDatabase()
    }
}