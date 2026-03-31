package com.example.book_worm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.book_worm.API.NetworkClient
import com.example.book_worm.DTOs.BookClub
import com.example.book_worm.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MyBookClubs(
    onBrowsePublicClubs: () -> Unit,
    onCreateClub: () -> Unit,
    onTabSelected: (BottomTab) -> Unit = {},
    onClubClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val hardcodedClub = BookClub(
        id = java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"),
        name = "Reading Rats",
        memberCount = 10,
        bookCount = 1,
        activeSince = "29/03/2026",
        currentlyReading = "To Kill a Mockingbird",
        drawableRes = R.drawable.reading_rats_icon
    )

    var bookClubs by remember { mutableStateOf<List<BookClub>>(listOf(hardcodedClub)) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            val token = UserContext.token
            if (token != null) {
                try {
                    val response = NetworkClient.bookClub.getMyBookClubs("Bearer $token")
                    if (response.isSuccessful) {
                        val apiClubs = response.body() ?: emptyList()
                        // Merge: keep hardcoded club, add any API clubs that aren't already in the list
                        val merged = (listOf(hardcodedClub) + apiClubs)
                            .distinctBy { it.id }
                        bookClubs = merged
                    }
                    // if API fails, keep showing the hardcoded club silently
                } catch (e: Exception) {
                    // network error — keep hardcoded club visible, no error shown
                }
            }
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(LightGreen)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = LightGreen,
            bottomBar = {
                Column {
                    // Browse + Create buttons pinned above the tab bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGreen)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onBrowsePublicClubs,
                            shape = RoundedCornerShape(33),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightestGreen,
                                contentColor = DarkGreen
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                            modifier = Modifier.weight(1f).padding(end = 12.dp)
                        ) {
                            Text(
                                text = "BROWSE PUBLIC BOOK\nCLUBS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp,
                                    color = DarkGreen
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = onCreateClub,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightestGreen,
                                contentColor = DarkGreen
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Text(
                                text = "+",
                                fontFamily = sulphur_point,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Light,
                                color = DarkGreen
                            )
                        }
                    }
                    BottomTabBar(
                        selectedTab = BottomTab.BookClub,
                        onTabSelected = onTabSelected
                    )
                }
            }
        ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Green)
                    .padding(top = 70.dp, bottom = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Book Clubs",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = sulphur_point,
                        fontSize = 24.sp,
                        color = DarkGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DarkGreen)
                    }
                }
                errorMessage.isNotEmpty() -> {
                    Text(
                        text = errorMessage,
                        color = BookAppleRed,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(bookClubs) { club ->
                            BookClubCard(club, onClick = onClubClick)
                        }
                    }
                }
            }
        } // end Column
        } // end Scaffold
    } // end Box
}

@Composable
fun BookClubCard(club: BookClub, onClick: () -> Unit = {}) {
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Green,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreen.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Club icon — white rounded box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                if (club.imageUrl != null) {
                    AsyncImage(
                        model = club.imageUrl,
                        contentDescription = club.name,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Fit
                    )
                } else if (club.drawableRes != null) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = club.drawableRes),
                        contentDescription = club.name,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(text = club.name.take(2).uppercase(), fontFamily = sulphur_point, fontSize = 20.sp, color = DarkGreen)
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Middle: club name + currently reading
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = club.name,
                    fontFamily = sulphur_point,
                    fontSize = 22.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    color = DarkGreen,
                    maxLines = 1
                )
                if (!club.currentlyReading.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = club.currentlyReading,
                        fontFamily = sulphur_point,
                        fontSize = 17.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkGreen.copy(alpha = 0.6f),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: stats right-aligned, no fixed width
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${club.memberCount} members",
                    fontFamily = sulphur_point,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGreen,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${club.bookCount} book(s)",
                    fontFamily = sulphur_point,
                    fontSize = 15.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGreen,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Active since ${club.activeSince}",
                    fontFamily = sulphur_point,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    color = DarkGreen,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}



