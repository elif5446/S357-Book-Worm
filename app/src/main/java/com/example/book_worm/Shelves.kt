package com.example.book_worm

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.book_worm.ui.theme.DarkGreen
import com.example.book_worm.ui.theme.Green
import com.example.book_worm.ui.theme.sulphur_point

private val ShelvesBodyGreen = Color(0xFFE3F0AF)

@Composable
fun Shelves(onTabSelected: (BottomTab) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ShelvesBodyGreen,
        bottomBar = {
            BottomTabBar(
                selectedTab = BottomTab.Shelves,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ShelvesBodyGreen)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green)
                        .padding(top = 70.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "My Shelves",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = sulphur_point,
                            fontSize = 24.sp,
                            color = DarkGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Green,
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, DarkGreen.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(83.dp)
                                .height(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF4FFD5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.to_kill_a_mockingbird),
                                contentDescription = "To Kill a Mockingbird",
                                modifier = Modifier
                                    .width(75.dp)
                                    .height(112.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Currently Reading",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen,
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = "To Kill a Mockingbird\nand 467 others",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = sulphur_point,
                                    fontSize = 16.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF3C5104).copy(alpha = 0.6f),
                                    textAlign = TextAlign.Start
                                ),
                                maxLines = 2
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF4FFD5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Add",
                                        tint = Color(0xFF3C5104)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
