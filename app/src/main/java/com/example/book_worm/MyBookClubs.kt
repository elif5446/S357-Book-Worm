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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Club image / placeholder
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            if (club.imageUrl != null) {
                AsyncImage(
                    model = club.imageUrl,
                    contentDescription = club.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else if (club.drawableRes != null) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = club.drawableRes),
                    contentDescription = club.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = club.name.take(2).uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = sulphur_point,
                        fontSize = 22.sp,
                        color = DarkGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = club.name,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = sulphur_point,
                fontSize = 21.sp,
                lineHeight = 20.sp,
                color = DarkGreen,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 2,
            modifier = Modifier.weight(1.1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${club.memberCount} members",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = sulphur_point,
                    fontSize = 14.sp,
                    lineHeight = 15.sp,
                    color = DarkGreen
                )
            )
            Text(
                text = "${club.bookCount} books",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = sulphur_point,
                    fontSize = 14.sp,
                    lineHeight = 15.sp,
                    color = DarkGreen
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Active since ${club.activeSince}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = sulphur_point,
                    fontSize = 10.sp,
                    lineHeight = 11.sp,
                    color = DarkGreen
                )
            )
        }
    }
}
