package com.example.book_worm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.book_worm.ui.theme.BookWormTheme

class MainActivity : ComponentActivity() {
    enum class Views {
        Login,
        Registration
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentView by remember { mutableStateOf(Views.Login) }
            BookWormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when(currentView) {
                        Views.Login -> Login { currentView = Views.Registration }
                        Views.Registration -> Registration { currentView = Views.Login }
                    }
                }
            }
        }
    }
}