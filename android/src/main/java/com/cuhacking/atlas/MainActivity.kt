package com.cuhacking.atlas

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mapbox.mapboxsdk.maps.Style
import com.cuhacking.atlas.databinding.ActivityMainBinding
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync { map ->
            map.setStyle(Style.DARK) { style ->
                // I took some artistic liberties while figuring out how to do this

                // FeatureCollection from MultiPolygon GeoJson
                val featureCollection = FeatureCollection.fromJson(giveJson())
                val geoJsonSource = GeoJsonSource("geojson-source", featureCollection)
                style.addSource(geoJsonSource)

                val fillLayer = FillLayer("fill-layer", "geojson-source")
                fillLayer.setProperties(PropertyFactory.fillColor(Color.DKGRAY))
                style.addLayer(fillLayer)

                //! https://stackoverflow.com/a/40286827
                //  not android sdk but originally found below
                //! https://github.com/mapbox/mapbox-gl-js/issues/3018
                val outlineLayer = LineLayer("outline-layer", "geojson-source")
                outlineLayer.setProperties(
                    PropertyFactory.lineColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.purple_700
                        )
                    ),
                    PropertyFactory.lineWidth(5f)
                )
                style.addLayer(outlineLayer)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    // hide the long json :)
    private fun giveJson() =
        """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[[-75.69827377796173,45.39140195535068],[-75.69802701473236,45.39140195535068],[-75.69802701473236,45.392049912726584],[-75.69827377796173,45.392049912726584],[-75.69827377796173,45.39140195535068]]]}},{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[[-75.69746375083923,45.39141325699649],[-75.69719552993774,45.39141325699649],[-75.69719552993774,45.392049912726584],[-75.69746375083923,45.392049912726584],[-75.69746375083923,45.39141325699649]]]}},{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[[-75.69878876209258,45.39117215472943],[-75.69864392280579,45.390949887665876],[-75.69835960865021,45.39088584445143],[-75.69794118404388,45.390870775449265],[-75.69744229316711,45.39085947369498],[-75.69701850414276,45.39089337895103],[-75.69679319858551,45.390953654911506],[-75.69669127464294,45.39110434453151],[-75.69677174091339,45.39119852533998],[-75.69687902927399,45.39109681006006],[-75.6971150636673,45.39102146529023],[-75.69743692874908,45.39101016356611],[-75.69775879383087,45.39099886183969],[-75.69805383682251,45.391044068731716],[-75.69833815097809,45.39113824964064],[-75.69861173629761,45.39125503374973],[-75.69878876209258,45.39126256820012],[-75.69878876209258,45.39117215472943]]]}}]}"""
}