package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.components.ConfettiCelebrationEffect
import com.example.ui.components.EcoGameData
import com.example.ui.components.EcoGameType
import com.example.ui.components.GameLeaderboardSection
import com.example.ui.components.GameStreakCard
import com.example.ui.components.InteractiveEcoGameDialog
import com.example.ui.components.StreakMilestoneCelebrationDialog
import com.example.ui.theme.EcoGreen
import com.example.ui.theme.GangaTealContainer
import com.example.ui.theme.GangaTealDark
import com.example.ui.theme.GangaTealPrimary
import com.example.ui.theme.GoldYellowContainer
import com.example.ui.theme.GoldYellowDark
import com.example.ui.theme.GoldYellowPrimary
import com.example.ui.theme.OnGoldYellowContainer

data class GameItem(
    val id: String,
    val title: String,
    val category: String,
    val entryFeeCoins: Int,
    val rewardTokens: Int,
    val rewardCoins: Int,
    val icon: String,
    val description: String
)

@Composable
fun GameZoneScreen(
    user: UserProfile,
    onPlayGame: (gameTitle: String, fee: Int, rewardTokens: Int, rewardCoins: Int) -> Unit
) {
    // Sanskowik 6.3 Interactive Eco-Games
    val ecoGames = listOf(
        EcoGameData(
            type = EcoGameType.RIVER_RESCUE,
            title = "River Rescue: Flowing Purity",
            description = "Help clear plastic and debris from the local river to save aquatic life.",
            ecoPointsReward = 50,
            icon = "🌊"
        ),
        EcoGameData(
            type = EcoGameType.GHAT_GUARDIAN,
            title = "Ghat Guardian: Heritage Cleanup",
            description = "Restore the sacred steps of the ghat using sustainable cleaning practices.",
            ecoPointsReward = 75,
            icon = "🏛️"
        ),
        EcoGameData(
            type = EcoGameType.ECO_SORT,
            title = "EcoSort: Waste Segregation Challenge",
            description = "Learn proper waste management by sorting items into correct bins.",
            ecoPointsReward = 40,
            icon = "♻️"
        )
    )

    val games = listOf(
        GameItem("1", "Action RPG", "Action", 20, 2, 50, "⚔️", "Embark on heroic action quests to defend and revitalize sacred ecosystems."),
        GameItem("2", "Strategy War", "Strategy", 25, 2, 60, "🛡️", "Master tactical real-time strategy battles and build river guardian alliances.")
    )

    var activeEcoGame by remember { mutableStateOf<EcoGameData?>(null) }
    var activeDialogGame by remember { mutableStateOf<GameItem?>(null) }
    var gameResultMessage by remember { mutableStateOf<String?>(null) }
    var showConfetti by remember { mutableStateOf(false) }
    var winStreak by remember { mutableIntStateOf(3) } // Starts with an active 3-game hot streak for instant visual feedback
    var showStreakMilestoneDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("game_zone_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🎮 Game Zone Desk",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Play games with eco-coins & win tokens!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GoldYellowContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldYellowPrimary)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🪙", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${user.tokens} Tokens",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = OnGoldYellowContainer
                            )
                        }
                    }
                }
            }

            // Visual Streak Indicator Card with Flame & Multipliers
            item {
                GameStreakCard(
                    streak = winStreak,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Sanskowik 6.3 Eco Gaming Zone Hero Card (Inspiring the Young Generation)
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = GangaTealContainer.copy(alpha = 0.8f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = GangaTealPrimary,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = "Eco",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Sanskowik6.3 Gaming Zone",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp,
                                        color = GangaTealDark
                                    )
                                    Text(
                                        text = "Inspiring the Young Generation 🌱",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GangaTealDark.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = GoldYellowContainer
                            ) {
                                Text(
                                    text = "3 Interactive Games",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnGoldYellowContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3 Eco-Games Cards
                        ecoGames.forEach { ecoGame ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { activeEcoGame = ecoGame }
                                    .testTag("eco_game_${ecoGame.type.name.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(GoldYellowContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(ecoGame.icon, fontSize = 22.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = ecoGame.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = ecoGame.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "⭐ +${ecoGame.ecoPointsReward} EcoPoints Reward",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = GangaTealDark
                                        )
                                    }

                                    Button(
                                        onClick = { activeEcoGame = ecoGame },
                                        colors = ButtonDefaults.buttonColors(containerColor = GangaTealPrimary),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("Play", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Active Game Result Banner
            if (gameResultMessage != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GangaTealContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = GangaTealDark,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = gameResultMessage!!,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = GangaTealDark,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { gameResultMessage = null }) {
                                Text("Dismiss", color = GangaTealDark)
                            }
                        }
                    }
                }
            }

            // Global Game Leaderboard Section (Chess, Ludo, Puzzle, Runner)
            item {
                GameLeaderboardSection()
            }

            // Games Header
            item {
                Text(
                    text = "Featured Tournaments & Arcade",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Games Items
            items(games) { game ->
                val streakBonusCoins = if (winStreak >= 2) (game.rewardCoins * (winStreak * 0.25)).toInt() else 0
                val streakBonusTokens = if (winStreak >= 3) 1 else 0

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(GoldYellowContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = game.icon, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = game.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = GangaTealContainer
                                    ) {
                                        Text(
                                            text = game.category,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GangaTealDark,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = game.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Fee: ${game.entryFeeCoins} Coins",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Win: +${game.rewardTokens + streakBonusTokens} Token & +${game.rewardCoins + streakBonusCoins} Coins",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GangaTealDark
                                    )
                                }
                                if (winStreak >= 2) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "🔥 Streak Bonus: +$streakBonusCoins Coins" + (if (streakBonusTokens > 0) " & +$streakBonusTokens Token" else "") + " included!",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { activeDialogGame = game },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldYellowPrimary,
                                contentColor = OnGoldYellowContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.SportsEsports, contentDescription = "Play", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Play", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Interactive Sanskowik6.3 Eco-Game Dialog
        if (activeEcoGame != null) {
            InteractiveEcoGameDialog(
                game = activeEcoGame!!,
                winStreak = winStreak,
                onDismiss = { activeEcoGame = null },
                onGameCompleted = { points, tokens, coins ->
                    val newStreak = winStreak + 1
                    winStreak = newStreak
                    onPlayGame(activeEcoGame!!.title, 0, tokens, coins)
                    gameResultMessage = "🎉 Won ${activeEcoGame!!.title}! Earned +$points EcoPoints & +$tokens Eco-Tokens (🔥 $newStreak-Game Streak)!"
                    showConfetti = true
                    if (newStreak >= 2) {
                        showStreakMilestoneDialog = true
                    }
                }
            )
        }

        // Confetti Particle Celebration Overlay
        if (showConfetti) {
            ConfettiCelebrationEffect(
                onFinished = { showConfetti = false }
            )
        }

        // Streak Milestone Celebration Dialog
        if (showStreakMilestoneDialog) {
            StreakMilestoneCelebrationDialog(
                streak = winStreak,
                onDismiss = { showStreakMilestoneDialog = false }
            )
        }
    }

    // Play Confirmation Dialog for Featured Arcade Tournaments
    if (activeDialogGame != null) {
        val game = activeDialogGame!!
        val streakBonusCoins = if (winStreak >= 1) ((game.rewardCoins) * ((winStreak + 1) * 0.25)).toInt() else 0
        val streakBonusTokens = if (winStreak >= 2) 1 else 0
        val totalRewardCoins = game.rewardCoins + streakBonusCoins
        val totalRewardTokens = game.rewardTokens + streakBonusTokens

        AlertDialog(
            onDismissRequest = { activeDialogGame = null },
            icon = { Text(game.icon, fontSize = 36.sp) },
            title = { Text("Launch " + game.title + "?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Entry fee: ${game.entryFeeCoins} Coins.")
                    Text("Potential victory reward: +$totalRewardTokens Eco-Tokens and +$totalRewardCoins Coins!")
                    if (winStreak >= 1) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldYellowContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🔥 Win this to advance to a ${winStreak + 1}-game winning streak!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnGoldYellowContainer,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newStreak = winStreak + 1
                        winStreak = newStreak
                        onPlayGame(game.title, game.entryFeeCoins, totalRewardTokens, totalRewardCoins)
                        gameResultMessage = "🎉 Victory in ${game.title}! Won +$totalRewardTokens Eco-Token & +$totalRewardCoins Coins (🔥 ${newStreak}-Win Streak)!"
                        showConfetti = true
                        if (newStreak >= 2) {
                            showStreakMilestoneDialog = true
                        }
                        activeDialogGame = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GangaTealPrimary)
                ) {
                    Text("Start Match & Win")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { activeDialogGame = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

