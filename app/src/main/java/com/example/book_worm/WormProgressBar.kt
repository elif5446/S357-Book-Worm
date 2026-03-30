package com.example.book_worm

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun WormProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = tween(durationMillis = 800),
        label = "worm_progress"
    )

    val bodyPink    = Color(0xFFE8639A)
    val stripePink  = Color(0xFFFF1493)   // bright pink for stripes
    val bodyLight   = Color(0xFFF7B8D0)
    val trackColor  = Color.White
    val eyeWhite    = Color.White
    val eyePupil    = Color(0xFF4A3728)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val radius = h / 2f

        // Track
        drawRoundRect(
            color = trackColor,
            size = Size(w, h),
            cornerRadius = CornerRadius(radius, radius)
        )

        // Worm body
        val filledWidth = (w - h) * animatedProgress + h
        if (animatedProgress > 0.02f) {
            drawRoundRect(
                color = bodyPink,
                size = Size(filledWidth, h),
                cornerRadius = CornerRadius(radius, radius)
            )

            // Bright pink segment stripes
            val segmentCount = 8
            val stripeWidth = 3.5f
            val bodyStart = radius
            val bodyEnd = filledWidth - radius
            val bodyLength = bodyEnd - bodyStart
            if (bodyLength > 0f) {
                for (i in 1 until segmentCount) {
                    val x = bodyStart + (bodyLength * i / segmentCount)
                    drawLine(
                        color = stripePink,
                        start = Offset(x, h * 0.15f),
                        end = Offset(x, h * 0.85f),
                        strokeWidth = stripeWidth
                    )
                }
                // Highlight sheen
                drawRoundRect(
                    color = bodyLight.copy(alpha = 0.35f),
                    topLeft = Offset(bodyStart, h * 0.1f),
                    size = Size(bodyLength, h * 0.3f),
                    cornerRadius = CornerRadius(h * 0.15f, h * 0.15f)
                )
            }
        }

        // Head circle
        val headCenterX = if (animatedProgress > 0.02f) filledWidth - radius else radius
        val headCenter = Offset(headCenterX, radius)

        drawCircle(color = bodyPink, radius = radius, center = headCenter)
        // Head highlight
        drawCircle(
            color = bodyLight.copy(alpha = 0.5f),
            radius = radius * 0.6f,
            center = Offset(headCenter.x - radius * 0.15f, headCenter.y - radius * 0.2f)
        )

        // TWO EYES — spaced wide apart on the head
        val eyeRadius = radius * 0.26f
        val eyeOffsetX = radius * 0.52f   // how far left/right from center

        // Left eye
        val leftEyeCenter = Offset(headCenter.x - eyeOffsetX, headCenter.y - radius * 0.12f)
        drawCircle(color = eyeWhite, radius = eyeRadius, center = leftEyeCenter)
        drawCircle(
            color = eyePupil,
            radius = eyeRadius * 0.55f,
            center = Offset(leftEyeCenter.x + eyeRadius * 0.1f, leftEyeCenter.y + eyeRadius * 0.1f)
        )
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.2f,
            center = Offset(leftEyeCenter.x - eyeRadius * 0.15f, leftEyeCenter.y - eyeRadius * 0.2f)
        )

        // Right eye
        val rightEyeCenter = Offset(headCenter.x + eyeOffsetX, headCenter.y - radius * 0.12f)
        drawCircle(color = eyeWhite, radius = eyeRadius, center = rightEyeCenter)
        drawCircle(
            color = eyePupil,
            radius = eyeRadius * 0.55f,
            center = Offset(rightEyeCenter.x + eyeRadius * 0.1f, rightEyeCenter.y + eyeRadius * 0.1f)
        )
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.2f,
            center = Offset(rightEyeCenter.x - eyeRadius * 0.15f, rightEyeCenter.y - eyeRadius * 0.2f)
        )

        // Little bow on top of head
        val bowCenterX = headCenter.x
        val bowCenterY = headCenter.y - radius * 1.05f
        val bowLoopW = radius * 0.55f
        val bowLoopH = radius * 0.38f

        // Left loop of bow
        drawOval(
            color = stripePink,
            topLeft = Offset(bowCenterX - bowLoopW * 1.6f, bowCenterY - bowLoopH),
            size = Size(bowLoopW * 1.3f, bowLoopH * 2f)
        )
        drawOval(
            color = stripePink.copy(alpha = 0.55f),
            topLeft = Offset(bowCenterX - bowLoopW * 1.45f, bowCenterY - bowLoopH * 0.6f),
            size = Size(bowLoopW * 0.7f, bowLoopH * 0.9f)
        )

        // Right loop of bow
        drawOval(
            color = stripePink,
            topLeft = Offset(bowCenterX + bowLoopW * 0.3f, bowCenterY - bowLoopH),
            size = Size(bowLoopW * 1.3f, bowLoopH * 2f)
        )
        drawOval(
            color = stripePink.copy(alpha = 0.55f),
            topLeft = Offset(bowCenterX + bowLoopW * 0.75f, bowCenterY - bowLoopH * 0.6f),
            size = Size(bowLoopW * 0.7f, bowLoopH * 0.9f)
        )

        // Center knot of bow
        drawCircle(
            color = stripePink,
            radius = radius * 0.18f,
            center = Offset(bowCenterX, bowCenterY)
        )
    }
}



