package com.example.book_worm

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.book_worm.ui.theme.DarkGreen
import com.example.book_worm.ui.theme.Green
import com.example.book_worm.ui.theme.LightestGreen
import com.example.book_worm.ui.theme.sulphur_point
import com.example.book_worm.API.NetworkClient
import com.example.book_worm.DTOs.ReviewCreate
import com.example.book_worm.DTOs.Review
import com.example.book_worm.DTOs.Comment
import com.example.book_worm.DTOs.CommentCreate
import com.example.book_worm.DTOs.ReactionCreate

private val SearchHeaderGreen = Green
private val SearchBodyGreen = Color(0xFFE3F0AF)

@Composable
fun Search(onTabSelected: (BottomTab) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDetails by remember { mutableStateOf(false) }
    val hasMockingbirdResult = searchQuery.contains("mocking", ignoreCase = true)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SearchBodyGreen,
        bottomBar = {
            BottomTabBar(
                selectedTab = BottomTab.Search,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SearchBodyGreen)
        ) {
            val headerInnerWidth = maxWidth - 36.dp

            Column(modifier = Modifier.fillMaxSize()) {
                LaunchedEffect(showDetails) {
                    if (!showDetails) focusRequester.requestFocus()
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SearchHeaderGreen)
                        .padding(start = 18.dp, end = 18.dp, top = 70.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (showDetails) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "To Kill a Mockingbird",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF3C5104)
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.ChevronLeft,
                            contentDescription = "Back",
                            tint = Color(0xFF3C5104),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(48.dp)
                                .padding(start = 8.dp)
                                .clickable { showDetails = false }
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val filterSize = 44.dp
                            val maxSearchWidth = headerInnerWidth - filterSize - 10.dp
                            val searchBarWidth = if (maxSearchWidth < 344.dp) maxSearchWidth else 344.dp
                            val textFieldWidth = searchBarWidth - 44.dp

                            Row(
                                modifier = Modifier
                                    .width(searchBarWidth)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(LightestGreen)
                                    .padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Search",
                                    tint = Color(0xFF610E0F)
                                )
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier
                                        .width(textFieldWidth)
                                        .focusRequester(focusRequester)
                                        .onFocusChanged { focusState ->
                                            if (focusState.isFocused) keyboardController?.show()
                                        },
                                    textStyle = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = sulphur_point,
                                        fontSize = 20.sp,
                                        letterSpacing = 0.sp,
                                        color = DarkGreen
                                    ),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        if (searchQuery.isBlank()) {
                                            Text(
                                                text = "Book, author...",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontFamily = sulphur_point,
                                                    fontSize = 20.sp,
                                                    letterSpacing = (0).sp,
                                                    color = Color(0xFF3C5104)
                                                )
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(LightestGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    contentDescription = "Filter",
                                    tint = DarkGreen
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(if (showDetails) 4.dp else 20.dp))

                if (!showDetails) {
                    if (!hasMockingbirdResult) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Search for a book\nor an author!",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 22.sp,
                                    lineHeight = 24.sp,
                                    color = Color(0xFF3C5104)
                                )
                            )
                        }
                    }
                    if (hasMockingbirdResult) {
                        Spacer(modifier = Modifier.height(4.dp))
                        SearchResultCard { showDetails = true }
                    }
                } else {
                    BookDetailContent()
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Green,
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreen.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(74.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF4FFD5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.to_kill_a_mockingbird),
                    contentDescription = "To Kill a Mockingbird cover",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "To Kill a Mockingbird",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 24.sp,
                            letterSpacing = 0.22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3C5104)
                        )
                    )
                    Text(
                        text = "Harper Lee",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 22.sp,
                            letterSpacing = 0.22.sp,
                            color = Color(0x993C5104)
                        )
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "3.95",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = sulphur_point,
                                fontSize = 22.sp,
                                letterSpacing = 0.22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3C5104)
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.apple),
                            contentDescription = "Apple rating",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookDetailContent() {
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoadingReviews by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    fun refreshReviews() {
        coroutineScope.launch {
            isLoadingReviews = true
            try {
                val response = NetworkClient.review.getReviewsForBook("to-kill-a-mockingbird")
                if (response.isSuccessful && response.body() != null) {
                    reviews = response.body()!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingReviews = false
            }
        }
    }
    
    LaunchedEffect(Unit) {
        refreshReviews()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.Top
        ) {
                Icon(
                    painter = painterResource(id = R.drawable.to_kill_a_mockingbird),
                    contentDescription = "To Kill a Mockingbird",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .width(176.dp)
                        .height(268.dp)
                )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                    Text(
                        text = "To Kill a Mockingbird",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3C5104)
                        )
                    )
                    Text(
                    text = "Harper Lee",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = sulphur_point,
                        fontSize = 22.sp,
                        color = Color(0x993C5104)
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Overall 3.95",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3C5104)
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = "Apple rating",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A gripping, heart-wrenching, and wholly remarkable coming-of-age tale in a South poisoned by virulent prejudice.",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = sulphur_point,
                        fontSize = 16.sp,
                        letterSpacing = 0.16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF3C5104)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        BookDetailButtons(onReviewPosted = { refreshReviews() })

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF3C5104))
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Friend Activity",
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = sulphur_point,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3C5104)
            )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (isLoadingReviews) {
            Text(
                text = "Loading reviews...",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = sulphur_point,
                    fontSize = 14.sp,
                    color = Color(0xFF3C5104)
                )
            )
        } else if (reviews.isEmpty()) {
            Text(
                text = "No reviews yet",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = sulphur_point,
                    fontSize = 14.sp,
                    color = Color(0xFF3C5104)
                )
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews) { review ->
                    ReviewRow(review = review)
                }
            }
        }
    }
}

@Composable
private fun BookDetailButtons(onReviewPosted: () -> Unit) {
    val options = listOf("Want to read", "Currently reading", "Read", "Favourites", "Owned", "Recs")
    var expanded by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewRating by remember { mutableStateOf(3) }
    var commentText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(192.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFF4FFD5))
                .clickable { expanded = true },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ADD TO SHELF",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = sulphur_point,
                        fontSize = 22.sp,
                        color = Color(0xFF3C5104),
                        letterSpacing = 0.01.em
                    )
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = "Dropdown",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF3C5104)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 0.dp, y = 8.dp),
                containerColor = Color(0xFFF4FFD5),
                shape = RoundedCornerShape(19.dp),
                modifier = Modifier.width(280.dp)
            ) {
                options.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = { expanded = false },
                        text = {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 22.sp,
                                    color = Color(0xFF3C5104)
                                )
                            )
                        }
                    )
                    if (index != options.lastIndex) {
                        HorizontalDivider(
                            color = Color(0xFF91A78B),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .width(129.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFF4FFD5))
                .clickable { showReviewDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "REVIEW",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = sulphur_point,
                    fontSize = 22.sp,
                    color = Color(0xFF3C5104),
                    letterSpacing = 0.01.em
                )
            )
        }
    }

    if (showReviewDialog) {
        Dialog(
            onDismissRequest = { showReviewDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF7F8962).copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-36).dp)
                        .width(306.dp)
                        .height(295.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(Color(0xFFF4FFD5))
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFC5D49A))
                            .clickable { showReviewDialog = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF3C5104),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Review of",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = sulphur_point,
                                fontSize = 22.sp,
                                color = Color(0xFF3C5104)
                            )
                        )
                        Text(
                            text = "To kill a mocking bird",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = sulphur_point,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3C5104)
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(246.dp)
                                .height(1.dp)
                                .background(Color(0xFF91A78B))
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mar 2026",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 30.dp),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = sulphur_point,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF3C5104)
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Your rating:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 18.sp,
                                    color = Color(0xFF3C5104)
                                )
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (index in 1..5) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.apple),
                                        contentDescription = "Rating $index",
                                        tint = if (index <= reviewRating) Color(0xFFA81622) else Color(0xFFD8C7AE),
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { reviewRating = index }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(86.dp)
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.55f))
                                .padding(10.dp)
                        ) {
                            BasicTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                modifier = Modifier.fillMaxSize(),
                                textStyle = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 16.sp,
                                    color = Color(0xFF3C5104)
                                ),
                                decorationBox = { innerTextField ->
                                    if (commentText.isBlank()) {
                                        Text(
                                            text = "Write your review...",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontFamily = sulphur_point,
                                                fontSize = 16.sp,
                                                color = Color(0x993C5104)
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .width(112.dp)
                                .height(34.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFC5D49A))
                                .clickable(enabled = !isSubmitting) {
                                    coroutineScope.launch {
                                        isSubmitting = true
                                        try {
                                            val currentUserId = UserContext.user?.ID?.toString()
                                                ?: "77266581-ff45-4847-8510-f6f609e69481"
                                            val currentUserName = UserContext.user?.username ?: "Anonymous"
                                            val payload = ReviewCreate(
                                                user_id = currentUserId,
                                                user_name = currentUserName,
                                                book_id = "to-kill-a-mockingbird",
                                                rating = reviewRating,
                                                comment = commentText.trim()
                                            )
                                            val response = NetworkClient.review.createReview(payload)
                                            if (response.isSuccessful) {
                                                commentText = ""
                                                reviewRating = 3
                                                showReviewDialog = false
                                                onReviewPosted()
                                                Toast.makeText(context, "Review posted", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Failed to post review", Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (_: Exception) {
                                            Toast.makeText(context, "Network error posting review", Toast.LENGTH_SHORT).show()
                                        } finally {
                                            isSubmitting = false
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isSubmitting) "..." else "POST",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 18.sp,
                                    color = Color(0xFF3C5104),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewRow(review: Review) {
    var showReactionPicker by remember { mutableStateOf(false) }
    var showCommentInput by remember { mutableStateOf(false) }
    var commentInput by remember { mutableStateOf("") }
    var reactions by remember(review.id) { mutableStateOf<List<com.example.book_worm.DTOs.Reaction>>(emptyList()) }
    var comments by remember(review.id) { mutableStateOf<List<Comment>>(emptyList()) }
    var isPostingComment by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(review.id) {
        try {
            val reactionsResponse = NetworkClient.reaction.getReactionsForReview(review.id)
            if (reactionsResponse.isSuccessful && reactionsResponse.body() != null) {
                reactions = reactionsResponse.body()!!
            }

            val response = NetworkClient.comment.getCommentsForReview(review.id)
            if (response.isSuccessful && response.body() != null) {
                comments = response.body()!!
            }
        } catch (_: Exception) {
        }
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFFBAD76B),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = review.user_name,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = sulphur_point,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3C5104)
                    )
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${review.rating}/5",
                            modifier = Modifier.padding(top = 1.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = sulphur_point,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3C5104)
                        )
                    )
                    repeat(review.rating) {
                        Icon(
                            painter = painterResource(id = R.drawable.apple),
                            contentDescription = "Rating",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            
            Text(
                text = review.comment,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = sulphur_point,
                    fontSize = 16.sp,
                    color = Color(0xFF3C5104)
                ),
                maxLines = 2
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(75.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF4FFD5))
                        .clickable { showReactionPicker = !showReactionPicker },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "React",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 16.sp,
                            color = Color(0xFF3C5104)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .width(95.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF4FFD5))
                        .clickable { showCommentInput = !showCommentInput },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Comment",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 16.sp,
                            color = Color(0xFF3C5104)
                        )
                    )
                }
            }
            
            if (showReactionPicker) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    val emojis = listOf("❤️", "😂", "😮", "😢", "👍")
                    emojis.forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFBAD76B))
                                .clickable {
                                    coroutineScope.launch {
                                        try {
                                            val currentUserId = UserContext.user?.ID?.toString()
                                                ?: "77266581-ff45-4847-8510-f6f609e69481"
                                            val reaction = ReactionCreate(
                                                user_id = currentUserId,
                                                review_id = review.id,
                                                reaction_type = emoji
                                            )
                                            val response = NetworkClient.reaction.createReaction(reaction)
                                            if (response.isSuccessful && response.body() != null) {
                                                reactions = reactions + response.body()!!
                                                showReactionPicker = false
                                            } else {
                                                Toast.makeText(context, "Failed to react", Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Network error reacting", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 14.sp)
                        }
                    }
                }
            }

            if (reactions.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    reactions
                        .groupBy { it.reaction_type }
                        .toList()
                        .forEach { (emoji, items) ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF4FFD5))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$emoji ${items.size}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = sulphur_point,
                                        fontSize = 12.sp,
                                        color = Color(0xFF3C5104)
                                    )
                                )
                            }
                        }
                }
            }

            if (showCommentInput) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        BasicTextField(
                            value = commentInput,
                            onValueChange = { commentInput = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = sulphur_point,
                                fontSize = 12.sp,
                                color = Color(0xFF3C5104)
                            ),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (commentInput.isBlank()) {
                                    Text(
                                        text = "Add a comment...",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = sulphur_point,
                                            fontSize = 12.sp,
                                            color = Color(0x993C5104)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkGreen)
                            .padding(horizontal = 10.dp)
                            .clickable(enabled = !isPostingComment && commentInput.isNotBlank()) {
                                coroutineScope.launch {
                                    isPostingComment = true
                                    try {
                                        val currentUserId = UserContext.user?.ID?.toString()
                                            ?: "77266581-ff45-4847-8510-f6f609e69481"
                                        val currentUserName = UserContext.user?.username ?: "Anonymous"
                                        val payload = CommentCreate(
                                            user_id = currentUserId,
                                            user_name = currentUserName,
                                            review_id = review.id,
                                            comment_text = commentInput.trim()
                                        )
                                        val response = NetworkClient.comment.createComment(payload)
                                        if (response.isSuccessful && response.body() != null) {
                                            comments = comments + response.body()!!
                                            commentInput = ""
                                            showCommentInput = false
                                        } else {
                                            Toast.makeText(context, "Failed to post comment", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "Network error posting comment", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        isPostingComment = false
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isPostingComment) "..." else "POST",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = sulphur_point,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = LightestGreen
                            )
                        )
                    }
                }
            }

            if (comments.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    comments.forEach { comment ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF4FFD5))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = comment.user_name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = sulphur_point,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF3C5104)
                                    )
                                )
                                Text(
                                    text = comment.comment_text,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = sulphur_point,
                                        fontSize = 13.sp,
                                        color = Color(0xFF3C5104)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
