package com.cuhacking.atlas

import android.app.Application
import com.cuhacking.atlas.common.AtlasConfig
import com.cuhacking.atlas.db.appContext
import com.mapbox.mapboxsdk.Mapbox

class AtlasApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        Mapbox.getInstance(applicationContext, AtlasConfig.MAPBOX_KEY)
    }
}
