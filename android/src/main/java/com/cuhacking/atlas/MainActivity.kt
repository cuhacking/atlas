package com.cuhacking.atlas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cuhacking.atlas.common.getMessage
import com.cuhacking.atlas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exampleText.text = getMessage()
    }
}