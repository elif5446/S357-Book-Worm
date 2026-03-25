package com.example.book_worm.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.book_worm.R

val sulphur_point = FontFamily(
    Font(R.font.sulphur_point_regular, FontWeight.Normal),
    Font(R.font.sulphur_point_light, FontWeight.Light),
    Font(R.font.sulphur_point_bold, FontWeight.Bold)
)

val sunshiney_regular = FontFamily(
    Font(R.font.sunshiney_regular, FontWeight.Normal),
)

// Set of Material typography styles to start with
val Typography = Typography(
    labelLarge = TextStyle(
        fontFamily = sulphur_point,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 35.sp,
        letterSpacing = 1.sp,
        color = DarkGreen
    ),
    labelMedium = TextStyle(
        fontFamily = sulphur_point,
        fontWeight = FontWeight.Normal,
        fontSize = 27.sp,
        lineHeight = 35.sp,
        letterSpacing = 1.sp,
        color = DarkGreen
    ),
    bodyLarge = TextStyle(
        fontFamily = sulphur_point,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 35.sp,
        letterSpacing = 1.sp,
        color = LightestGreen
    ),
    headlineLarge = TextStyle(
        fontFamily = sunshiney_regular,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 35.sp,
        letterSpacing = 1.sp,
        color = Black
    )
    /* bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ) */
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)