package com.cuhacking.atlas

import android.graphics.Color
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.cuhacking.atlas.common.exampleDataSource
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.databinding.ActivityMainBinding
import com.cuhacking.atlas.search.SearchResultsAdapter
import com.cuhacking.atlas.common.SearchViewModel
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val searchViewModel by lazy { SearchViewModel(CoroutineDispatchers, lifecycleScope) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync { map ->
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
                style.addLayer(outlineLayer("outline-layer", "example"))
            }
        }

        binding.searchResultsList.layoutManager = LinearLayoutManager(this)
        val searchResultsAdapter = SearchResultsAdapter {
            // Used for testing click handler
            Toast.makeText(this, it.name + " was clicked", Toast.LENGTH_SHORT).show()
        }
        binding.searchResultsList.adapter = searchResultsAdapter

        lifecycleScope.launch {
            searchViewModel.searchResults.collect { searchResultItems ->
                searchResultsAdapter.submitList(searchResultItems)
            }
        }

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                lifecycleScope.launch {
                    searchViewModel.getSearchResults(newText)
                }
                binding.searchResultsList.isVisible = true
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                lifecycleScope.launch {
                    searchViewModel.getSearchResults(query)
                }
                return true
            }
        })
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

    private fun outlineLayer(layerId: String, sourceId: String): LineLayer {
        val outlineLayer = LineLayer(layerId, sourceId)
        outlineLayer.setProperties(
            PropertyFactory.lineColor(
                ContextCompat.getColor(
                    this,
                    R.color.purple_700
                )
            ),
            @Suppress("MagicNumber")
            PropertyFactory.lineWidth(5f)
        )
        return outlineLayer
    }
}
