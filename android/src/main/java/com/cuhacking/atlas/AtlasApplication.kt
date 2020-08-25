package com.cuhacking.atlas

import android.app.Application
import com.cuhacking.atlas.common.mapboxKeyTemp
import com.cuhacking.atlas.db.appContext
import com.cuhacking.atlas.db.createDatabase
import com.cuhacking.atlas.db.database
import com.cuhacking.atlas.db.provideDbDriver
import com.mapbox.mapboxsdk.Mapbox

class AtlasApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        database = provideDbDriver().createDatabase()

        Mapbox.getInstance(applicationContext, mapboxKeyTemp)
    }
}