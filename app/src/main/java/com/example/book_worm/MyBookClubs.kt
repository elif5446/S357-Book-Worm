package com.example.book_worm

import androidx.compose.foundation.background
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
    onCreateClub: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val hardcodedClub = BookClub(
        id = java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"),
        name = "Reading Rats",
        memberCount = 10,
        bookCount = 1,
        activeSince = "29/03/2026",
        currentlyReading = "To Kill a Mockingbird",
        drawableRes = R.drawable.reading_rats
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
                        bookClubs = listOf(hardcodedClub) + apiClubs
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
        Column(modifier = Modifier.fillMaxSize()) {


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
                        contentPadding = PaddingValues(bottom = 120.dp) // space for bottom buttons
                    ) {
                        items(bookClubs) { club ->
                            BookClubCard(club)
                        }
                    }
                }
            }
        }

        // Bottom buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Browse Public Book Clubs" button
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

            // "+" create button
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
    }
}

@Composable
fun BookClubCard(club: BookClub) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Green)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Club image — square white box with rounded corners
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            if (club.imageUrl != null) {
                AsyncImage(
                    model = club.imageUrl,
                    contentDescription = club.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            } else if (club.drawableRes != null) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = club.drawableRes),
                    contentDescription = club.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = club.name.take(2).uppercase(),
                    fontFamily = sulphur_point,
                    fontSize = 22.sp,
                    color = DarkGreen
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Left column: club name (bold) + currently reading book title below
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = club.name,
                fontFamily = sulphur_point,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                color = DarkGreen,
                fontWeight = FontWeight.Bold
            )
            if (!club.currentlyReading.isNullOrEmpty()) {
                Text(
                    text = club.currentlyReading,
                    fontFamily = sulphur_point,
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    color = DarkGreen
                )
            }
        }

        // Right column: members, books, active since — all right-aligned, vertically centered
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Text(
                text = "${club.memberCount} members",
                fontFamily = sulphur_point,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                color = DarkGreen,
                textAlign = TextAlign.End
            )
            Text(
                text = "${club.bookCount} book(s)",
                fontFamily = sulphur_point,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                color = DarkGreen,
                textAlign = TextAlign.End
            )
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



