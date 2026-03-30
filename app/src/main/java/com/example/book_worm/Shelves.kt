package com.example.book_worm

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.book_worm.ui.theme.sulphur_point

private val ShelvesHeaderGreen = Color(0xFFBAD76B)
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ShelvesBodyGreen)
        ) {
            val headerHeight = maxHeight * 0.2f

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                        .background(ShelvesHeaderGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "My Shelves",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = sulphur_point,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF3C5104)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(353.dp)
                        .height(127.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFBAD76B)),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.to_kill_a_mockingbird),
                        contentDescription = "To Kill a Mockingbird",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                            .width(101.dp)
                            .height(152.dp)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 10.dp, start = 124.dp, end = 10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "currently reading",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = sulphur_point,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
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
                    }

                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color(0xFF3C5104),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 10.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}
