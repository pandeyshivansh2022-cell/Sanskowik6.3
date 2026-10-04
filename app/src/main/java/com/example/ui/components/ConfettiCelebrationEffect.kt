package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

data class ConfettiParticle(
    val xRatio: Float,
    val yOffset: Float,
    val color: Color,
    val size: Float,
    val speedX: Float,
    val speedY: Float,
    val rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiCelebrationEffect(
    onFinished: () -> Unit
) {
    val colors = remember {
        listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFF00B0FF), // Blue
            Color(0xFF00E676), // Green
            Color(0xFFFF4081), // Pink
            Color(0xFFFF9100)  // Orange
        )
    }

    val particles = remember {
        List(50) {
            ConfettiParticle(
                xRatio = Random.nextFloat(),
                yOffset = -Random.nextFloat() * 150f - 20f,
                color = colors.random(),
                size = Random.nextFloat() * 14f + 8f,
                speedX = (Random.nextFloat() - 0.5f) * 200f,
                speedY = Random.nextFloat() * 800f + 600f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
        )
        onFinished()
    }

    val currentProgress = progress.value

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEach { p ->
            val currentX = (p.xRatio * width + p.speedX * currentProgress) % width
            val normalizedX = if (currentX < 0) currentX + width else currentX
            val currentY = p.yOffset + p.speedY * currentProgress
            val currentRotation = p.rotation + p.rotationSpeed * currentProgress

            if (currentY in -50f..(height + 50f)) {
                rotate(currentRotation, pivot = Offset(normalizedX, currentY)) {
                    drawRect(
                        color = p.color.copy(alpha = (1f - currentProgress * 0.3f).coerceIn(0f, 1f)),
                        topLeft = Offset(normalizedX, currentY),
                        size = Size(p.size, p.size * 0.6f)
                    )
                }
            }
        }
    }
}
