package com.example.book_worm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.book_worm.API.NetworkClient
import com.example.book_worm.DTOs.UpdateProgressRequest
import com.example.book_worm.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun Chat(onTabSelected: (BottomTab) -> Unit = {}) {
    val scope = rememberCoroutineScope()
    val clubId = "00000000-0000-0000-0000-000000000001"
    val bookTotalPages = 330  // To Kill a Mockingbird

    var personalPages by remember { mutableStateOf(0) }
    var clubTotalPages by remember { mutableStateOf(0) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }

    suspend fun refreshProgress(token: String) {
        try {
            val r = NetworkClient.bookClub.getClubProgress("Bearer $token", clubId)
            if (r.isSuccessful) r.body()?.let { personalPages = it.currentPages }
        } catch (_: Exception) {}
        try {
            val r = NetworkClient.bookClub.getMembersProgress("Bearer $token", clubId)
            if (r.isSuccessful) clubTotalPages = r.body()?.sumOf { it.currentPages } ?: personalPages
        } catch (_: Exception) { clubTotalPages = personalPages }
    }

    LaunchedEffect(Unit) {
        val token = UserContext.token ?: return@LaunchedEffect
        refreshProgress(token)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = LightGreen,
        bottomBar = {
            BottomTabBar(selectedTab = BottomTab.Chat, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            //  HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Green)
                    .padding(top = 56.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Currently Reading",
                    fontFamily = sulphur_point,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    color = DarkGreen,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BOOK CARD
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                color = Green,
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Book cover — top left
                    Image(
                        painter = painterResource(id = R.drawable.to_kill_a_mockingbird),
                        contentDescription = "To Kill a Mockingbird",
                        modifier = Modifier
                            .width(75.dp)
                            .height(112.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Right column: title, author, worm, pages, buttons
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Title
                        Text(
                            text = "To Kill a Mockingbird",
                            fontFamily = sulphur_point,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGreen,
                            lineHeight = 20.sp
                        )
                        // Author
                        Text(
                            text = "Harper Lee",
                            fontFamily = sulphur_point,
                            fontSize = 14.sp,
                            color = DarkGreen.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Worm bar — starts at same x as title
                        WormProgressBar(
                            progress = (personalPages.toFloat() / bookTotalPages.toFloat()).coerceIn(0f, 1f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Pages count right-aligned above the buttons
                        Text(
                            text = "$personalPages/$bookTotalPages\npages",
                            fontFamily = sulphur_point,
                            fontSize = 12.sp,
                            color = DarkGreen.copy(alpha = 0.7f),
                            textAlign = TextAlign.End,
                            lineHeight = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // UPDATE PROGRESS + + button right-aligned in a row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showUpdateDialog = true },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF4FFD5),
                                    contentColor = DarkGreen
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "UPDATE PROGRESS",
                                    fontFamily = sulphur_point,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(0xFFF4FFD5),
                                shadowElevation = 2.dp,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { showUpdateDialog = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        fontFamily = sulphur_point,
                                        fontSize = 22.sp,
                                        color = DarkGreen,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // ── UPDATE PROGRESS DIALOG
    if (showUpdateDialog) {
        UpdateProgressDialog(
            currentPages = personalPages,
            goalPages = bookTotalPages,
            isLoading = isUpdating,
            onDismiss = { showUpdateDialog = false },
            onConfirm = { newPages ->
                isUpdating = true
                scope.launch {
                    val token = UserContext.token
                    if (token != null) {
                        try {
                            NetworkClient.bookClub.updateProgress(
                                "Bearer $token",
                                clubId,
                                UpdateProgressRequest(currentPages = newPages, clubId = clubId)
                            )
                            refreshProgress(token)
                        } catch (_: Exception) {
                            personalPages = newPages
                        }
                    } else {
                        personalPages = newPages
                    }
                    isUpdating = false
                    showUpdateDialog = false
                }
            }
        )
    }
}




