package com.example.book_worm

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
import com.example.book_worm.ui.theme.DarkGreen
import com.example.book_worm.ui.theme.Green
import com.example.book_worm.ui.theme.LightestGreen
import com.example.book_worm.ui.theme.sulphur_point

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
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                Spacer(modifier = Modifier.height(12.dp))
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
                Spacer(modifier = Modifier.height(16.dp))
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
        BookDetailButtons()

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
    }
}

@Composable
private fun BookDetailButtons() {
    val options = listOf("Want to read", "Currently reading", "Read", "Favourites", "Owned", "Recs")
    var expanded by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

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
                        .width(306.dp)
                        .height(295.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(Color(0xFFF4FFD5))
                ) {
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
                    }
                }
            }
        }
    }
}
