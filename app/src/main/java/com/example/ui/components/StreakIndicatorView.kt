package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GangaTealContainer
import com.example.ui.theme.GangaTealDark
import com.example.ui.theme.GangaTealPrimary
import com.example.ui.theme.GoldYellowContainer
import com.example.ui.theme.GoldYellowDark
import com.example.ui.theme.GoldYellowLight
import com.example.ui.theme.GoldYellowPrimary
import com.example.ui.theme.OnGoldYellowContainer

@Composable
fun GameStreakCard(
    streak: Int,
    modifier: Modifier = Modifier
) {
    // Pulse animation for the flame icon
    val infiniteTransition = rememberInfiniteTransition(label = "StreakPulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    val isStreakActive = streak >= 2
    val streakTierTitle = when {
        streak >= 5 -> "👑 Godlike Streak!"
        streak >= 4 -> "💥 Blazing Streak!"
        streak >= 3 -> "⚡ Hot Streak!"
        streak >= 2 -> "🔥 Warm Streak!"
        streak == 1 -> "🎯 1st Win Recorded"
        else -> "🎮 Ready for Win Streak"
    }

    val multiplierText = when {
        streak >= 5 -> "+100% Bonus Coins & +5 Bonus Tokens"
        streak >= 4 -> "+75% Bonus Coins & +3 Bonus Tokens"
        streak >= 3 -> "+50% Bonus Coins & +2 Bonus Tokens"
        streak >= 2 -> "+25% Bonus Coins & +1 Bonus Token"
        else -> "Win 2+ games in a row for Bonus Rewards!"
    }

    val backgroundGradient = if (isStreakActive) {
        Brush.horizontalGradient(
            listOf(
                Color(0xFFFFFAF0),
                GoldYellowContainer,
                Color(0xFFFFEDD5)
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        )
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isStreakActive) 6.dp else 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundGradient, RoundedCornerShape(20.dp))
            .border(
                width = if (isStreakActive) 2.dp else 1.dp,
                color = if (isStreakActive) GoldYellowPrimary.copy(alpha = glowAlpha) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("game_streak_indicator")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header row with animated fire badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (isStreakActive) {
                                    Brush.radialGradient(
                                        listOf(
                                            Color(0xFFFF7A00),
                                            Color(0xFFE11D48),
                                            GoldYellowDark
                                        )
                                    )
                                } else {
                                    Brush.radialGradient(
                                        listOf(
                                            GoldYellowContainer,
                                            GoldYellowLight.copy(alpha = 0.6f)
                                        )
                                    )
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (streak >= 5) "👑" else if (streak >= 3) "🔥" else if (streak >= 2) "⚡" else "🎮",
                            fontSize = 24.sp,
                            modifier = if (isStreakActive) Modifier.scale(pulseScale) else Modifier
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = streakTierTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isStreakActive) OnGoldYellowContainer else MaterialTheme.colorScheme.onSurface
                            )
                            if (isStreakActive) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = GoldYellowPrimary
                                ) {
                                    Text(
                                        text = "${streak}X COMBO",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = multiplierText,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isStreakActive) GangaTealDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Counter badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isStreakActive) Color(0xFFFF7A00) else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.testTag("streak_count_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak Fire",
                            tint = if (isStreakActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(16.dp)
                                .scale(if (isStreakActive) pulseScale else 1f)
                        )
                        Text(
                            text = "$streak Wins",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isStreakActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Milestone Track (1 -> 2 -> 3 -> 4 -> 5+)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val stages = listOf(
                    1 to "1 Win",
                    2 to "2 (1.2x)",
                    3 to "3 (1.5x)",
                    4 to "4 (2.0x)",
                    5 to "5+ (3.0x)"
                )

                stages.forEach { (targetStreak, label) ->
                    val isReached = streak >= targetStreak
                    val isCurrent = streak == targetStreak || (targetStreak == 5 && streak >= 5)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> GoldYellowPrimary
                                        isReached -> GangaTealPrimary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                                .border(
                                    width = if (isCurrent) 2.dp else 0.dp,
                                    color = if (isCurrent) Color(0xFFFF7A00) else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isReached) "✓" else "$targetStreak",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isReached || isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrent) OnGoldYellowContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StreakMilestoneCelebrationDialog(
    streak: Int,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "StreakCelebrationPulse")
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CelebrationScale"
    )

    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CelebrationRotate"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("streak_celebration_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Animated Fire / Trophy Icon with gradient aura
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFFFFD54F),
                                    Color(0xFFFF7A00),
                                    Color(0xFFE11D48)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (streak >= 5) "👑" else if (streak >= 3) "🔥" else "⚡",
                        fontSize = 44.sp,
                        modifier = Modifier.scale(scaleAnim)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "🔥 ${streak}-WIN STREAK UNLOCKED! 🔥",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFE11D48),
                        fontSize = 18.sp
                    )

                    Text(
                        text = "Unstoppable momentum! You're on fire across Game Zone tournaments.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Reward Multiplier Banner
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GoldYellowContainer,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldYellowPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Bonus",
                            tint = GoldYellowDark,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+${streak * 25}% Bonus Coins & Multiplier Active!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = OnGoldYellowContainer
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldYellowPrimary,
                        contentColor = OnGoldYellowContainer
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Continue",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Keep Winning!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
