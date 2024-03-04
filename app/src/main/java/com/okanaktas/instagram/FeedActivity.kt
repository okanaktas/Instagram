package com.okanaktas.instagram

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.okanaktas.instagram.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}