package com.example.book_worm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Popup
import com.example.book_worm.API.NetworkClient
import com.example.book_worm.DTOs.ChapterComment
import com.example.book_worm.DTOs.PostCommentRequest
import com.example.book_worm.ui.theme.*
import kotlinx.coroutines.launch

private const val TOTAL_CHAPTERS = 31

@Composable
fun ChapterComments(
    onBack: () -> Unit,
    onTabSelected: (BottomTab) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val clubId = "00000000-0000-0000-0000-000000000001"

    var selectedChapter by remember { mutableStateOf(1) }
    var showChapterDropdown by remember { mutableStateOf(false) }

    // Per-chapter comment cache — keeps locally posted comments alive when switching chapters
    val commentsCache = remember { mutableStateMapOf<Int, List<ChapterComment>>() }
    // Track which chapters have already been fetched so we don't re-fetch and wipe local posts
    val fetchedChapters = remember { mutableSetOf<Int>() }

    var isLoadingComments by remember { mutableStateOf(false) }

    var showWriteArea by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val chipBg = Color(0xFFBAD76B)         // Green
    val commentBg = Color(0xFFCDEA8B)
    val postButtonBg = Color(0xFFF4FFD5)

    // Current chapter's comment list (live from cache)
    val comments = commentsCache[selectedChapter] ?: emptyList()

    // Fetch comments for a chapter only once (first visit); locally posted comments are kept
    LaunchedEffect(selectedChapter) {
        showWriteArea = false
        commentText = ""
        if (!fetchedChapters.contains(selectedChapter)) {
            isLoadingComments = true
            val token = UserContext.token
            if (token != null) {
                try {
                    val response = NetworkClient.bookClub.getChapterComments(
                        "Bearer $token", clubId, selectedChapter
                    )
                    if (response.isSuccessful) {
                        commentsCache[selectedChapter] = response.body() ?: emptyList()
                    } else {
                        commentsCache.getOrPut(selectedChapter) { emptyList() }
                    }
                } catch (_: Exception) {
                    commentsCache.getOrPut(selectedChapter) { emptyList() }
                }
            } else {
                commentsCache.getOrPut(selectedChapter) { emptyList() }
            }
            fetchedChapters.add(selectedChapter)
            isLoadingComments = false
        }
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
            ) {

                // ── HEADER ───────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightGreen)
                ) {
                    // Close button (×) — top right of header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, end = 12.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Surface(
                            modifier = Modifier.size(34.dp),
                            shape = CircleShape,
                            color = Color(0xFFC5D49A),
                            shadowElevation = 6.dp,
                            tonalElevation = 0.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onBack() },
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy((-2).dp)
                        ) {
                            Text(
                                text = "To Kill a Mockingbird",
                                fontFamily = sulphur_point,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Harper Lee",
                                fontFamily = sulphur_point,
                                fontSize = 15.sp,
                                color = DarkGreen.copy(alpha = 0.55f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // ── CHAPTER NAVIGATION ROW ───────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // PREVIOUS — takes equal weight on the left
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            TextButton(
                                onClick = { if (selectedChapter > 1) selectedChapter-- },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = "< PREVIOUS",
                                    fontFamily = sulphur_point,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedChapter > 1) DarkGreen else DarkGreen.copy(alpha = 0.3f)
                                )
                            }
                        }

                        // Chapter chip + dropdown — centered in the middle
                        Box(contentAlignment = Alignment.Center) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = chipBg,
                                shadowElevation = 3.dp,
                                modifier = Modifier.clickable { showChapterDropdown = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "CHAPTER $selectedChapter",
                                        fontFamily = sulphur_point,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkGreen
                                    )
                                    Text(text = "▼", fontSize = 10.sp, color = DarkGreen)
                                }
                            }

                            if (showChapterDropdown) {
                                val dropdownListState = rememberLazyListState()
                                Popup(
                                    alignment = Alignment.TopCenter,
                                    offset = IntOffset(0, 0),
                                    onDismissRequest = { showChapterDropdown = false }
                                ) {
                                    val thumbRatio = 4f / TOTAL_CHAPTERS.toFloat()
                                    val scrollFraction = if (dropdownListState.layoutInfo.totalItemsCount > 4) {
                                        dropdownListState.firstVisibleItemIndex.toFloat() /
                                                (dropdownListState.layoutInfo.totalItemsCount - 4).toFloat()
                                    } else 0f

                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0xFF93B437),
                                        shadowElevation = 4.dp,
                                        modifier = Modifier
                                            .width(144.dp)
                                            .height(168.dp)
                                    ) {
                                        Box {
                                            LazyColumn(
                                                state = dropdownListState,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(end = 10.dp) // leave room for scrollbar
                                            ) {
                                                items(TOTAL_CHAPTERS) { index ->
                                                    val ch = index + 1
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                selectedChapter = ch
                                                                showChapterDropdown = false
                                                            }
                                                            .padding(horizontal = 12.dp, vertical = 10.dp)
                                                    ) {
                                                        Text(
                                                            text = "CHAPTER $ch",
                                                            fontFamily = sulphur_point,
                                                            fontSize = 14.sp,
                                                            fontWeight = if (ch == selectedChapter) FontWeight.Bold else FontWeight.Normal,
                                                            color = DarkGreen
                                                        )
                                                    }
                                                    if (ch < TOTAL_CHAPTERS) {
                                                        HorizontalDivider(
                                                            color = DarkGreen.copy(alpha = 0.35f),
                                                            thickness = 1.dp
                                                        )
                                                    }
                                                }
                                            }

                                            // Scrollbar
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.CenterEnd)
                                                    .padding(end = 3.dp, top = 8.dp, bottom = 8.dp)
                                                    .width(4.dp)
                                                    .fillMaxHeight()
                                            ) {
                                                // Track
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(RoundedCornerShape(2.dp))
                                                        .background(Color.White.copy(alpha = 0.25f))
                                                )
                                                // Thumb
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight(thumbRatio)
                                                        .align(Alignment.TopStart)
                                                        .offset(y = with(androidx.compose.ui.platform.LocalDensity.current) {
                                                            val trackHeight = 168.dp - 16.dp
                                                            val thumbHeight = trackHeight * thumbRatio
                                                            (trackHeight - thumbHeight) * scrollFraction
                                                        })
                                                        .clip(RoundedCornerShape(2.dp))
                                                        .background(Color.White.copy(alpha = 0.75f))
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        } // end chip Box

                        // NEXT — takes equal weight on the right
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(
                                onClick = { if (selectedChapter < TOTAL_CHAPTERS) selectedChapter++ },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = "NEXT >",
                                    fontFamily = sulphur_point,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedChapter < TOTAL_CHAPTERS) DarkGreen else DarkGreen.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = DarkGreen.copy(alpha = 0.2f)
                    )
                }

                // ── COMMENTS LIST + WRITE AREA ───────────────────────────────
                // This column fills all remaining space and puts WRITE COMMENT at bottom
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Comments list — takes all available space above the button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (isLoadingComments) {
                            CircularProgressIndicator(
                                color = DarkGreen,
                                modifier = Modifier
                                    .size(28.dp)
                                    .align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                contentPadding = PaddingValues(bottom = 8.dp)
                            ) {
                                if (comments.isEmpty()) {
                                    item {
                                        Text(
                                            text = "No comments yet for this chapter.\nBe the first to share your thoughts!",
                                            fontFamily = sulphur_point,
                                            fontSize = 14.sp,
                                            color = DarkGreen.copy(alpha = 0.55f),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 40.dp)
                                        )
                                    }
                                } else {
                                    items(comments) { comment ->
                                        ChapterCommentBubble(comment = comment, bubbleBg = commentBg)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ── Write area OR Write Comment button ───────────────────
                    if (showWriteArea) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = chipBg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                TextField(
                                    value = commentText,
                                    onValueChange = { commentText = it },
                                    placeholder = {
                                        Text(
                                            text = "Share your thoughts...",
                                            fontFamily = sulphur_point,
                                            fontSize = 14.sp
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = DarkGreen,
                                        unfocusedTextColor = DarkGreen,
                                        cursorColor = DarkGreen,
                                        focusedPlaceholderColor = DarkGreen.copy(alpha = 0.5f),
                                        unfocusedPlaceholderColor = DarkGreen.copy(alpha = 0.5f)
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontFamily = sulphur_point,
                                        fontSize = 14.sp
                                    ),
                                    minLines = 2,
                                    maxLines = 5
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            val text = commentText.trim()
                                            if (text.isNotEmpty() && !isPosting) {
                                                isPosting = true
                                                scope.launch {
                                                    val token = UserContext.token
                                                    val username = UserContext.user?.username ?: "You"
                                                    val newComment = ChapterComment(
                                                        clubId = clubId,
                                                        chapterNumber = selectedChapter,
                                                        username = username,
                                                        content = text
                                                    )
                                                    val chapterAtPost = selectedChapter
                                                    val postedComment: ChapterComment = if (token != null) {
                                                        try {
                                                            val response = NetworkClient.bookClub.postChapterComment(
                                                                "Bearer $token",
                                                                clubId,
                                                                PostCommentRequest(
                                                                    clubId = clubId,
                                                                    chapterNumber = chapterAtPost,
                                                                    content = text
                                                                )
                                                            )
                                                            if (response.isSuccessful) response.body() ?: newComment
                                                            else newComment
                                                        } catch (_: Exception) {
                                                            newComment
                                                        }
                                                    } else {
                                                        newComment
                                                    }
                                                    // Append into the cache so it survives chapter switching
                                                    val current = commentsCache[chapterAtPost] ?: emptyList()
                                                    commentsCache[chapterAtPost] = current + postedComment
                                                    // Mark as fetched so we don't wipe it on next visit
                                                    fetchedChapters.add(chapterAtPost)
                                                    commentText = ""
                                                    showWriteArea = false
                                                    isPosting = false
                                                    // Scroll to bottom safely
                                                    val newSize = commentsCache[chapterAtPost]?.size ?: 0
                                                    if (newSize > 0) {
                                                        listState.animateScrollToItem(newSize - 1)
                                                    }
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(50),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = postButtonBg,
                                            contentColor = DarkGreen
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                                        enabled = !isPosting && commentText.isNotBlank()
                                    ) {
                                        if (isPosting) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(14.dp),
                                                color = DarkGreen,
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Text(
                                                text = "POST",
                                                fontFamily = sulphur_point,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DarkGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // WRITE COMMENT pill button — bottom right, matches mockup
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Button(
                                onClick = { showWriteArea = true },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = chipBg,
                                    contentColor = DarkGreen
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                contentPadding = PaddingValues(horizontal = 36.dp, vertical = 16.dp)
                            ) {
                                Text(
                                    text = "WRITE COMMENT",
                                    fontFamily = sulphur_point,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterCommentBubble(
    comment: ChapterComment,
    bubbleBg: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = comment.username,
            fontFamily = sulphur_point,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = bubbleBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = comment.content,
                fontFamily = sulphur_point,
                fontSize = 14.sp,
                color = DarkGreen,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}



