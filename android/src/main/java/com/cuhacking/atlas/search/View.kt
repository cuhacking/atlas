package com.cuhacking.atlas.search

import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cuhacking.atlas.MainActivity
import com.cuhacking.atlas.common.SearchResult
import com.cuhacking.atlas.common.exampleDataSource
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val viewModel = MainActivity().searchViewModel
val searchResultItems: MutableState<List<SearchResult>> = mutableStateOf(viewModel.searchResults.value)
val searchResultsVisible = mutableStateOf(false)

@Composable
@Preview(showBackground = true)
fun SearchBar() {
    var query by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        elevation = (10.dp),
        modifier = Modifier
            .height(75.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        TextField(
            backgroundColor = Color.White,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            value = query,
            onValueChange = {
                query = it
                coroutineScope.launch {
                    viewModel.getSearchResults(query)
                    searchResultsVisible.value = true
                    viewModel.searchResults.collect {
                        searchResultItems.value = it
                    }
                }},
            leadingIcon = { Icon(Icons.Filled.Search, "Search") },
            placeholder = { Text("Search...") },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    coroutineScope.launch {
                        viewModel.getSearchResults(query)
                        query = ""
                        searchResultsVisible.value = false
                        viewModel.searchResults.collect {
                            searchResultItems.value = it
                        }
                    }
                }
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    MapView()
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar()
        searchResultList(searchResultItems.value, searchResultsVisible.value)
    }
}

@Composable
fun SearchResultView(searchResult: SearchResult, onClick: () -> Unit) {
    Card (
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(onClick = onClick)
            .border(width = 0.5.dp, color = Color.LightGray),
        backgroundColor = Color.White
    ){ Column (
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(horizontal = 10.dp),

            horizontalAlignment = Alignment.Start,
                )
        {
            Text(searchResult.name, style = typography.body1)
            Text(searchResult.description, style = typography.body2, color = Color.DarkGray)
        }
    }
}

@Composable
fun searchResultList(results: List<SearchResult>, isVisible: Boolean) {
    if (isVisible) {
        LazyColumn {
            items(items = results) { SearchResult ->
                SearchResultView(searchResult = SearchResult, onClick = { /*TODO*/ })
            }
        }
    }
}

//@Preview
@Composable
fun MapView(savedInstanceState: Bundle?) {
    AndroidView(viewBlock = { context ->
        com.mapbox.mapboxsdk.maps.MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val options = MapboxMapOptions.createFromAttributes(context)
            val camera = CameraPosition.DEFAULT
            camera.target.latitude = 45.386438
            camera.target.longitude = -75.696127
//            camera.zoom = 14.661
            options.camera(camera)
            onCreate(savedInstanceState)

//            getMapAsync { map ->
//                map.setStyle(Style.DARK) { style ->
//                    // I took some artistic liberties while figuring out how to do this
//
//                    // FeatureCollection from MultiPolygon GeoJson
//                    style.addSource(exampleDataSource)
//
//                    val fillLayer = FillLayer("fill-layer", "example")
//                    fillLayer.setProperties(PropertyFactory.fillColor(android.graphics.Color.DKGRAY))
//                    style.addLayer(fillLayer)
//
//                    // https://stackoverflow.com/a/40286827
//                    //  not android sdk but originally found below
//                    // https://github.com/mapbox/mapbox-gl-js/issues/3018
//                    style.addLayer(MainActivity().outlineLayer("outline-layer", "example"))
//                }
//            }
        }
    })
}

