package com.cuhacking.atlas

import android.app.Application
import android.content.Context
import com.cuhacking.atlas.common.AtlasConfig
import com.cuhacking.atlas.common.di.dataRepository
import com.cuhacking.atlas.db.*
import com.mapbox.mapboxsdk.Mapbox

class AtlasApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        dataRepository.downloadOrUpdateData()

        Mapbox.getInstance(applicationContext, AtlasConfig.MAPBOX_KEY)
    }
}
