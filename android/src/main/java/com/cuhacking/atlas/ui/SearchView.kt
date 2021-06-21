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
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.cuhacking.atlas.MainActivity
import com.cuhacking.atlas.common.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

val viewModel = MainActivity().searchViewModel
val searchResultsVisible = mutableStateOf(false)
val searchResultsFlow = viewModel.searchResults.flow

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
                }
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    coroutineScope.launch {
                        viewModel.getSearchResults(query)
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

@ExperimentalMaterialApi
@Suppress("MagicNumber")
@Composable
fun SearchResultView(searchResult: SearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(width = 0.5.dp, color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)),
        onClick = onClick
    ) { Column(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(searchResult.name, style = typography.body1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(searchResult.description, style = typography.body2)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SearchResultsList(results: Flow<List<SearchResult>>, isVisible: Boolean) {
    val searchResults = results.collectAsState(emptyList())
    val context = LocalContext.current

    if (isVisible) {
        LazyColumn {
            items(items = searchResults.value) { searchResult ->
                SearchResultView(searchResult = searchResult, onClick = {
                    Toast.makeText(context, "${searchResult.name} was clicked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
