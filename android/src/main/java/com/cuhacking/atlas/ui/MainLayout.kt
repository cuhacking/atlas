package com.cuhacking.atlas.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainLayout() {
    Box(modifier = Modifier.fillMaxSize()) {
        MapView()
        Column {
            SearchBar()
            SearchResultsList(searchResultItems.value, searchResultsVisible.value)
        }
    }
}
