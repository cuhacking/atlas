package com.cuhacking.atlas.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.cuhacking.atlas.R
import com.cuhacking.atlas.common.exampleDataSource
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory

@Composable
fun MapView() {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView)
}

@Suppress("MagicNumber")
@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val options = MapboxMapOptions.createFromAttributes(context)
        .camera(CameraPosition.Builder()
            .zoom(14.661)
            .target(LatLng(45.386438, -75.696127))
            .build()
        )

    val mapView = remember {
        MapView(context, options)
    }

    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return mapView
}

@Composable
private fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

@Composable
private fun MapViewContainer(map: MapView) {
    AndroidView({ map }) { mapView ->
        mapView.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            getMapAsync { map ->
                map.setStyle(Style.DARK) { style ->
                    // I took some artistic liberties while figuring out how to do this

                    // FeatureCollection from MultiPolygon GeoJson
                    style.addSource(exampleDataSource)

                    val fillLayer = FillLayer("fill-layer", "example")
                    fillLayer.setProperties(PropertyFactory.fillColor(Color.DKGRAY))
                    style.addLayer(fillLayer)

                    // https://stackoverflow.com/a/40286827
                    //  not android sdk but originally found below
                    // https://github.com/mapbox/mapbox-gl-js/issues/3018
                    style.addLayer(outlineLayer(context, "outline-layer", "example"))
                }

                map.addOnMapClickListener {
                    requestFocus()
                    searchResultsVisible.value = false
                    true
                }
            }
        }
    }
}

private fun outlineLayer(context: Context, layerId: String, sourceId: String): LineLayer {
    val outlineLayer = LineLayer(layerId, sourceId)
    outlineLayer.setProperties(
        PropertyFactory.lineColor(
            ContextCompat.getColor(
                context,
                R.color.purple_700
            )
        ),
        @Suppress("MagicNumber")
        PropertyFactory.lineWidth(5f)
    )
    return outlineLayer
}
