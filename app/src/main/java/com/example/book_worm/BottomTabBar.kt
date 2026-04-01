package com.example.book_worm

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val TabInactive = Color(0xFFBAD76B)
private val TabActive = Color(0xFF8FBF0B)

enum class BottomTab(@DrawableRes val iconRes: Int) {
    Chat(R.drawable.bookmark_icon),
    Shelves(R.drawable.shelves_icon),
    BookClub(R.drawable.chat_icon),
    Search(R.drawable.search_icon),
    Profile(R.drawable.profile_icon)
}

@Composable
fun BottomTabBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
    ) {
        val segmentWidth = maxWidth / 5

        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            val tabs = BottomTab.values()
            for (index in tabs.indices) {
                val tab = tabs[index]
                val isSelected = selectedTab == tab
                val background = if (isSelected) TabActive else TabInactive

                TextButton(
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .background(background)
                ) {
                    Icon(
                        painter = painterResource(id = tab.iconRes),
                        contentDescription = tab.name,
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}
