package com.example.book_worm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp as dpUnit
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.book_worm.ui.theme.Black
import com.example.book_worm.ui.theme.DarkGreen
import com.example.book_worm.ui.theme.sulphur_point

private val HeaderGreen = Color(0xFFBAD76B)
private val BodyGreen = Color(0xFFE3F0AF)
private val SoftCardGreen = Color(0xFFF4FFD5)
private val WormPink = Color(0xFFE98EA8)
private val ChallengeButtonGreen = Color(0xFF93B437)

@Composable
fun Profile(onTabSelected: (BottomTab) -> Unit, onClubClick: () -> Unit = {}) {
    var bioText by remember {
        mutableStateOf("Welcome to my page.\nFavourite genres: fantasy, classics, mystery.")
    }
    val username = UserContext.user?.username
        ?.takeIf { it.isNotBlank() }
        ?: UserContext.user?.email?.substringBefore("@")
            ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        ?: "User"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BodyGreen,
        bottomBar = {
            BottomTabBar(
                selectedTab = BottomTab.Profile,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BodyGreen)
        ) {
            val headerHeight = maxHeight * 0.48f

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                        .background(HeaderGreen)
                        .padding(24.dp)
                ) {
                    Text(
                        text = username,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = sulphur_point,
                            fontWeight = FontWeight.Normal,
                            fontSize = 30.sp,
                            color = DarkGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RabbitAvatar()
                        StatsSection(modifier = Modifier.fillMaxWidth())
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    BioCard(
                        text = bioText,
                        onTextChange = { bioText = it }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reader type:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = sulphur_point,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkGreen
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Avid",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = sulphur_point,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftCardGreen,
                                contentColor = Color(0xFF424242)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text(
                                text = "EDIT PROFILE",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.height(14.dp))

                ReadingChallengeSection(
                    completedBooks = 16,
                    totalBooks = 38,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(1.dp)
                        .background(DarkGreen.copy(alpha = 0.22f))
                )

                Spacer(modifier = Modifier.height(20.dp))

                BookClubsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClubClick = onClubClick
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun BookClubsSection(modifier: Modifier = Modifier, onClubClick: () -> Unit = {}) {
    Column(modifier = modifier) {
        Text(
            text = "Book Clubs",
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = sulphur_point,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        ClubCard(
            clubName = "Reading Rats",
            members = 10,
            totalBooksRead = 1,
            onClick = onClubClick
        )
    }
}

@Composable
private fun ClubCard(
    clubName: String,
    members: Int,
    totalBooksRead: Int,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = HeaderGreen,
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
            // Icon box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.reading_rats_icon),
                    contentDescription = "Reading Rats icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Middle: club name + currently reading
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = clubName,
                    fontFamily = sulphur_point,
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    color = DarkGreen,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "To Kill a Mockingbird",
                    fontFamily = sulphur_point,
                    fontSize = 17.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Normal,
                    color = DarkGreen.copy(alpha = 0.6f),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: stats right-aligned, no fixed width
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$members members",
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
                    text = "$totalBooksRead book(s)",
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
                    text = "Active since 29/03/2026",
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

@Composable
private fun ReadingChallengeSection(
    completedBooks: Int,
    totalBooks: Int,
    modifier: Modifier = Modifier
) {
    val progress = (completedBooks.toFloat() / totalBooks.toFloat()).coerceIn(0f, 1f)

    BoxWithConstraints(modifier = modifier) {
        val columnGap = 8.dp
        val leftWidth = maxWidth * 0.28f
        val centerWidth = maxWidth - leftWidth - columnGap
        val editButtonWidth = 86.dp
        val barWidth = centerWidth - editButtonWidth - 8.dp

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(columnGap),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.width(leftWidth)
            ) {
                Text(
                    text = "2026",
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = sulphur_point,
                        fontSize = 19.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkGreen
                    )
                )
                Text(
                    text = "Reading",
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = sulphur_point,
                        fontSize = 19.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkGreen
                    )
                )
                Text(
                    text = "Challenge",
                    maxLines = 1,
                    softWrap = false,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = sulphur_point,
                        fontSize = 19.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkGreen
                    )
                )
            }

            Column(
                modifier = Modifier.width(centerWidth),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WormProgressBar(
                        progress = progress,
                        modifier = Modifier
                            .weight(1f)
                            .height(22.dp)
                    )
                    Spacer(modifier = Modifier.width(editButtonWidth))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$completedBooks/$totalBooks\nBooks",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 17.sp,
                            lineHeight = 17.sp,
                            color = Black
                        )
                    )
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChallengeButtonGreen,
                            contentColor = Color(0xFF3F3F3F)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .width(editButtonWidth)
                            .height(36.dp)
                    ) {
                        Text(
                            text = "EDIT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = sulphur_point,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.2.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// WormProgressBar is defined in WormProgressBar.kt

@Composable
private fun RabbitAvatar() {
    Box(
        modifier = Modifier
            .size(70.dp)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = Color.White.copy(alpha = 0.6f),
                spotColor = Color.White.copy(alpha = 0.6f)
            )
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "🐰",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
            modifier = Modifier.offset(y = (-1).dp)
        )
    }
}

@Composable
private fun StatsSection(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val cardWidth = (maxWidth - 44.dp) / 3

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatCard(number = "20", label = "Books Read", modifier = Modifier.width(cardWidth))
            StatCard(number = "1", label = "Book Clubs", modifier = Modifier.width(cardWidth))
            StatCard(number = "10", label = "Friends", modifier = Modifier.width(cardWidth))
        }
    }
}

@Composable
private fun StatCard(number: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(84.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCardGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = number,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = sulphur_point,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = sulphur_point,
                    fontSize = 16.sp,
                    lineHeight = 17.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF5B5B5B)
                ),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun BioCard(text: String, onTextChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 14.dp, shape = RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCardGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = false,
            maxLines = 6,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontFamily = sulphur_point,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF3F3F3F)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 76.dp, max = 108.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            decorationBox = { innerTextField ->
                if (text.isBlank()) {
                    Text(
                        text = "Write your bio and favourite genres...",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 16.sp,
                            color = Color(0xFF9A9A9A)
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}
