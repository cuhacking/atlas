package com.cuhacking.atlas.ui

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.SearchResult
import com.cuhacking.atlas.common.SearchViewModel
import com.cuhacking.atlas.db.database
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val viewModel = SearchViewModel(database, CoroutineDispatchers)
val searchResultItems: MutableState<List<SearchResult>> = mutableStateOf(viewModel.searchResults.value)
val searchResultsVisible = mutableStateOf(false)

@Suppress("MagicNumber")
@Composable
fun SearchBar() {
    var query by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

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
            value = query,
            onValueChange = { textFieldValue: String ->
                query = textFieldValue
                coroutineScope.launch {
                    viewModel.getSearchResults(query)
                    searchResultsVisible.value = true
                    viewModel.searchResults.collect {
                        searchResultItems.value = it
                    }
                }
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    coroutineScope.launch {
                        viewModel.getSearchResults(query)
                        viewModel.searchResults.collect {
                            searchResultItems.value = it
                        }
                    }
                    focusManager.clearFocus(true)
                }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            leadingIcon = {
                Icon(Icons.Filled.Search, "Search")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(Icons.Filled.Clear, "Clear", modifier = Modifier.clickable(true, onClick = {
                        query = ""
                        searchResultsVisible.value = false
                    }))
                }
            },
            placeholder = { Text("Search...") },
        )
    }
}

@Suppress("MagicNumber")
@Composable
fun SearchResultView(searchResult: SearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(onClick = onClick)
            .border(width = 0.5.dp, color = Color.LightGray),
    ) { Column(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(searchResult.name, style = typography.body1)
            Text(searchResult.description, style = typography.body2, color = Color.DarkGray)
        }
    }
}

@Composable
fun SearchResultsList(results: List<SearchResult>, isVisible: Boolean) {
    val context = LocalContext.current
    if (isVisible) {
        LazyColumn {
            items(items = results) { searchResult ->
                SearchResultView(searchResult = searchResult, onClick = {
                    Toast.makeText(context, "${searchResult.name} was clicked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
