package com.example.book_worm

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.book_worm.API.NetworkClient
import com.example.book_worm.DTOs.UpdateProgressRequest
import com.example.book_worm.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun BookClubDetail(
    onBack: () -> Unit,
    onTabSelected: (BottomTab) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    // Hardcoded club ID for Reading Rats (matches the hardcoded club)
    val clubId = "00000000-0000-0000-0000-000000000001"
    val goalPages = 3300

    var currentPages by remember { mutableStateOf(10) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) }
    var updateError by remember { mutableStateOf("") }

    // Fetch live progress from backend on load
    LaunchedEffect(Unit) {
        val token = UserContext.token ?: return@LaunchedEffect
        try {
            val response = NetworkClient.bookClub.getClubProgress("Bearer $token", clubId)
            if (response.isSuccessful) {
                response.body()?.let { currentPages = it.currentPages }
            }
        } catch (_: Exception) { /* keep hardcoded value */ }
    }
    Box(modifier = Modifier.fillMaxSize().background(LightGreen)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = LightGreen,
            bottomBar = {
                BottomTabBar(
                    selectedTab = BottomTab.BookClub,
                    onTabSelected = onTabSelected
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {

                // ── HEADER ─────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green)
                        .padding(top = 56.dp, bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Back arrow
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 4.dp, top = 0.dp)
                            .size(56.dp)
                    ) {
                        Text("←", fontFamily = sulphur_point, fontSize = 40.sp, color = DarkGreen)
                    }
                    Text(
                        text = "Reading Rats\nBook Club",
                        fontFamily = sulphur_point,
                        fontSize = 28.sp,
                        lineHeight = 34.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkGreen,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── CURRENTLY READING ───────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT: text takes remaining space
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(
                            text = "Currently Reading",
                            fontFamily = sulphur_point,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGreen
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "To Kill a Mockingbird",
                            fontFamily = sulphur_point,
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkGreen
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Harper Lee",
                            fontFamily = sulphur_point,
                            fontSize = 20.sp,
                            color = DarkGreen.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rating row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Overall",
                                fontFamily = sulphur_point,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                            Text(
                                text = "4.27",
                                fontFamily = sulphur_point,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkGreen
                            )
                            Image(
                                painter = painterResource(id = R.drawable.apple),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp).offset(y = (-4).dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it. Winner of the Pulitzer Prize.",
                            fontFamily = sulphur_point,
                            fontSize = 13.sp,
                            lineHeight = 17.sp,
                            color = DarkGreen.copy(alpha = 0.95f),
                            maxLines = 10
                        )
                    }

                    // RIGHT: book cover — fixed size, centered vertically
                    Image(
                        painter = painterResource(id = R.drawable.to_kill_a_mockingbird),
                        contentDescription = "To Kill a Mockingbird cover",
                        modifier = Modifier
                            .width(150.dp)
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── DIVIDER ─────────────────────────────────────────────────
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = DarkGreen.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── MONTHLY READING GOAL ────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Monthly Reading Goal",
                        fontFamily = sulphur_point,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 🐛 WORM PROGRESS BAR — full width matching the bounds below
                    WormProgressBar(
                        progress = currentPages.toFloat() / goalPages.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(22.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // UPDATE PROGRESS button on left, pages count on right
                    // both within the same horizontal bounds as the worm bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showUpdateDialog = true },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green,
                                contentColor = DarkGreen
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
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

                        Text(
                            text = "$currentPages/$goalPages\npages",
                            fontFamily = sulphur_point,
                            fontSize = 16.sp,
                            textAlign = TextAlign.End,
                            lineHeight = 20.sp,
                            color = DarkGreen
                        )
                    }

                    if (updateError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = updateError,
                            color = BookAppleRed,
                            fontSize = 12.sp,
                            fontFamily = sulphur_point
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── DIVIDER ─────────────────────────────────────────────────
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = DarkGreen.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── CHAPTER COMMENTS BUTTON ─────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green,
                            contentColor = DarkGreen
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "CHAPTER COMMENTS",
                            fontFamily = sulphur_point,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } // end Box

    // ── UPDATE PROGRESS DIALOG ───────────────────────────────────────────
    if (showUpdateDialog) {
        UpdateProgressDialog(
            currentPages = currentPages,
            goalPages = goalPages,
            isLoading = isUpdating,
            onDismiss = { showUpdateDialog = false },
            onConfirm = { newPages ->
                isUpdating = true
                updateError = ""
                scope.launch {
                    val token = UserContext.token
                    if (token != null) {
                        try {
                            val response = NetworkClient.bookClub.updateProgress(
                                "Bearer $token",
                                clubId,
                                UpdateProgressRequest(currentPages = newPages, clubId = clubId)
                            )
                            if (response.isSuccessful) {
                                currentPages = response.body()?.currentPages ?: newPages
                            } else {
                                // API not ready yet — update locally so UI still works
                                currentPages = newPages
                            }
                        } catch (_: Exception) {
                            // No backend yet — update locally
                            currentPages = newPages
                        }
                    } else {
                        // Not logged in — still update locally for demo
                        currentPages = newPages
                    }
                    isUpdating = false
                    showUpdateDialog = false
                }
            }
        )
    }
}

// ── UPDATE PROGRESS DIALOG ───────────────────────────────────────────────────
@Composable
fun UpdateProgressDialog(
    currentPages: Int,
    goalPages: Int,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val inputPages = inputText.toIntOrNull() ?: 0
    val totalPages = 330 // To Kill a Mockingbird page count

    val modalBg = Color(0xFFF4FFD5)
    val buttonBg = Color(0xFF93B437)
    val buttonBorder = Color(0xFF6E8C28)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = modalBg,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title section — compact
                    Text(
                        text = "Progress Update",
                        fontFamily = sulphur_point,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "To Kill a Mockingbird",
                        fontFamily = sulphur_point,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Thin divider
                    HorizontalDivider(
                        color = DarkGreen.copy(alpha = 0.25f),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // "Now at page ___ / 330" row with underline input
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Now at page ",
                            fontFamily = sulphur_point,
                            fontSize = 18.sp,
                            color = DarkGreen
                        )
                        androidx.compose.foundation.text.BasicTextField(
                            value = inputText,
                            onValueChange = { inputText = it.filter { c -> c.isDigit() }.take(4) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = sulphur_point,
                                fontSize = 18.sp,
                                color = DarkGreen,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .width(60.dp)
                                .padding(bottom = 2.dp)
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    val y = size.height - strokeWidth / 2
                                    drawLine(
                                        color = DarkGreen.copy(alpha = 0.5f),
                                        start = androidx.compose.ui.geometry.Offset(0f, y),
                                        end = androidx.compose.ui.geometry.Offset(size.width, y),
                                        strokeWidth = strokeWidth
                                    )
                                }
                        )
                        Text(
                            text = " / $totalPages",
                            fontFamily = sulphur_point,
                            fontSize = 18.sp,
                            color = DarkGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // FINISHED and UPDATE buttons — same color, slim pill
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onConfirm(totalPages) },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonBg,
                                contentColor = DarkGreen,
                                disabledContainerColor = buttonBg.copy(alpha = 0.6f),
                                disabledContentColor = DarkGreen.copy(alpha = 0.5f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Text(
                                text = "FINISHED",
                                fontFamily = sulphur_point,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkGreen
                            )
                        }

                        Button(
                            onClick = { if (inputPages > 0) onConfirm(inputPages.coerceAtMost(totalPages)) },
                            shape = RoundedCornerShape(999.dp),
                            enabled = !isLoading && inputPages > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = buttonBg,
                                contentColor = DarkGreen,
                                disabledContainerColor = buttonBg.copy(alpha = 0.6f),
                                disabledContentColor = DarkGreen.copy(alpha = 0.5f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), color = DarkGreen, strokeWidth = 2.dp)
                            } else {
                                Text(
                                    text = "UPDATE",
                                    fontFamily = sulphur_point,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DarkGreen
                                )
                            }
                        }
                    }
                }

                // Circular X close button — #C5D49A with bottom shadow
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC5D49A))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "×",
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

// WormProgressBar is defined in WormProgressBar.kt











































