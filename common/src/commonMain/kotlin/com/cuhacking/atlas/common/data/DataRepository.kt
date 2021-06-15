package com.cuhacking.atlas.common.data

import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.DataCache
import com.cuhacking.atlas.common.FeatureApi
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlin.js.JsExport

@JsExport
class DataRepository(
    private val dispatchers: CoroutineDispatchers,
    private val dataCache: DataCache,
    private val featureApi: FeatureApi
) {
    private val scope = MainScope()

    private val _data = MutableStateFlow<String?>(null)

    /**
     * A flow of map data that is fetched via the FeatureApi. This flow automatically
     * updates when the cached data is updated.
     */
    val data = _data.asStateFlow()

    init {
        scope.launch(dispatchers.io) {
            try {
                _data.value = dataCache.readData()
            } catch (e: SerializationException) {
                // TODO: Log error
            } catch (e: IOException) {
                // Ignore
            }
        }
    }

    fun downloadOrUpdateData() = scope.launch(dispatchers.io) {
        featureApi.getAndStoreFeatures()
        _data.value = dataCache.readData()
    }
}