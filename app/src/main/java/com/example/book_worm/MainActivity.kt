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
import androidx.compose.ui.platform.LocalContext
import com.example.book_worm.API.TokenManager
import com.example.book_worm.ui.theme.BookWormTheme

class MainActivity : ComponentActivity() {
    enum class Views {
        Login,
        Registration,
        MyBookClubs
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentView by remember { mutableStateOf(Views.Login) }
            val savedToken = TokenManager.getToken(LocalContext.current)
            if (savedToken != null) {
                UserContext.token = savedToken
                currentView = Views.MyBookClubs
            }
            BookWormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when(currentView) {
                        Views.Login -> Login(
                            onNavigateToRegister = { currentView = Views.Registration },
                            onLoginSuccess = { currentView = Views.MyBookClubs }
                        )
                        Views.Registration -> Registration(
                            onNavigateToLogin = { currentView = Views.Login },
                            onRegisterSuccess = { currentView = Views.MyBookClubs }
                        )
                        Views.MyBookClubs -> MyBookClubs(
                            onBrowsePublicClubs = { /* TODO: Browse public clubs screen */ },
                            onCreateClub = { /* TODO: Create club screen */ }
                        )
                    }
                }
            }
        }
    }
}