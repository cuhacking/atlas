package com.cuhacking.atlas

import android.app.Application
import com.cuhacking.atlas.common.mapboxKeyTemp
import com.mapbox.mapboxsdk.Mapbox

class AtlasApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Mapbox.getInstance(applicationContext, mapboxKeyTemp)
    }
}