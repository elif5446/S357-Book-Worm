package com.example.book_worm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
        MyBookClubs,
        Chat,
        Shelves,
        BookClub,
        Search,
        Profile,
        ReadingRats
    }

    private fun tabToView(tab: BottomTab): Views {
        return when (tab) {
            BottomTab.Chat -> Views.Chat
            BottomTab.Shelves -> Views.Shelves
            BottomTab.BookClub -> Views.MyBookClubs
            BottomTab.Search -> Views.Search
            BottomTab.Profile -> Views.Profile
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentView by remember { mutableStateOf(Views.Login) }
            val context = LocalContext.current
            val savedToken = TokenManager.getToken(context)
            if (savedToken != null) {
                UserContext.token = savedToken
                UserContext.user = TokenManager.getUser(context)
                currentView = Views.Profile
            }
            BookWormTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when(currentView) {
                        Views.Login -> Login(
                            onNavigateToRegister = { currentView = Views.Registration },
                            onLoginSuccess = { currentView = Views.Profile }
                        )
                        Views.Registration -> Registration(
                            onNavigateToLogin = { currentView = Views.Login },
                            onRegisterSuccess = { currentView = Views.Profile }
                        )
                        Views.MyBookClubs -> MyBookClubs(
                            onBrowsePublicClubs = { /* TODO: Browse public clubs screen */ },
                            onCreateClub = { /* TODO: Create club screen */ },
                            onTabSelected = { currentView = tabToView(it) },
                            onClubClick = { currentView = Views.ReadingRats }
                        )
                        Views.Chat -> TabPlaceholderScreen(
                            label = "Chat",
                            currentTab = BottomTab.Chat,
                            onTabSelected = { currentView = tabToView(it) }
                        )
                        Views.Shelves -> Shelves(onTabSelected = { currentView = tabToView(it) })
                        Views.BookClub -> TabPlaceholderScreen(
                            label = "Book Club",
                            currentTab = BottomTab.BookClub,
                            onTabSelected = { currentView = tabToView(it) }
                        )
                        Views.Search -> Search(onTabSelected = { currentView = tabToView(it) })
                        Views.Profile -> Profile(
                            onTabSelected = { currentView = tabToView(it) },
                            onClubClick = { currentView = Views.ReadingRats }
                        )
                        Views.ReadingRats -> BookClubDetail(
                            onBack = { currentView = Views.MyBookClubs },
                            onTabSelected = { currentView = tabToView(it) }
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun TabPlaceholderScreen(
    label: String,
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomTabBar(
                selectedTab = currentTab,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "$label page",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
