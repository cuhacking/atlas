package com.cuhacking.atlas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.lifecycleScope
import com.cuhacking.atlas.common.CoroutineDispatchers
import com.cuhacking.atlas.common.SearchViewModel
import com.cuhacking.atlas.db.sharedDatabase
import com.cuhacking.atlas.ui.MainLayout

class MainActivity : AppCompatActivity() {
    val searchViewModel by lazy {
        SearchViewModel(
            CoroutineDispatchers,
            lifecycleScope,
            sharedDatabase
        )
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainLayout()
        }
    }
}
