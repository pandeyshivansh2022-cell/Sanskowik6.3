package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

enum class EcoGameType {
    RIVER_RESCUE,
    GHAT_GUARDIAN,
    ECO_SORT
}

data class EcoGameData(
    val type: EcoGameType,
    val title: String,
    val description: String,
    val ecoPointsReward: Int,
    val icon: String
)

@Composable
fun InteractiveEcoGameDialog(
    game: EcoGameData,
    winStreak: Int,
    onDismiss: () -> Unit,
    onGameCompleted: (pointsEarned: Int, tokensEarned: Int, bonusCoins: Int) -> Unit
) {
    var gameState by remember { mutableStateOf<GameState>(GameState.Playing) }
    var currentItemIndex by remember { mutableIntStateOf(0) }
    var userFeedback by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    // Debris items for River Rescue
    var riverDebrisList by remember {
        mutableStateOf(
            listOf(
                "🍾 Plastic Bottle",
                "🛍️ Polythene Bag",
                "🥤 Styrofoam Cup",
                "🥫 Discarded Can"
            )
        )
    }

    // Waste sorting challenge questions
    val sortingQuestions = remember {
        listOf(
            SortingQuestion(
                item = "Old Glass Bottle 🍾",
                correctCategory = WasteCategory.RECYCLABLE,
                explanation = "Glass can be endlessly recycled into new containers without losing purity or quality!"
            ),
            SortingQuestion(
                item = "Sacred Puja Flowers 🪷",
                correctCategory = WasteCategory.ORGANIC,
                explanation = "Floral offerings should be converted into natural temple compost and organic agarbatti!"
            ),
            SortingQuestion(
                item = "Single-use Polythene Bag 🛍️",
                correctCategory = WasteCategory.RECYCLABLE,
                explanation = "Plastic waste should be sent to specialized polymer recycling units rather than water bodies!"
            ),
            SortingQuestion(
                item = "Clay Diya & Coconut Shell 🥥",
                correctCategory = WasteCategory.ORGANIC,
                explanation = "Natural clay and coconut husks decompose organically and enrich riverbank flora!"
            )
        )
    }

    val streakBonusPoints = if (winStreak >= 2) (game.ecoPointsReward * (winStreak * 0.25)).toInt() else 0
    val totalPointsToReward = game.ecoPointsReward + streakBonusPoints
    val tokensToReward = if (game.ecoPointsReward >= 75) 2 else 1

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("interactive_eco_game_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = GoldYellowContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(game.icon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Sanskowik6.3 Eco Game",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnGoldYellowContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = game.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnGoldYellowContainer
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GangaTealPrimary
                        ) {
                            Text(
                                text = "+$totalPointsToReward Points",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Game Content based on Type & State
                if (gameState == GameState.Playing) {
                    when (game.type) {
                        EcoGameType.RIVER_RESCUE -> {
                            RiverRescueGameContent(
                                debrisList = riverDebrisList,
                                onDebrisRemoved = { debris ->
                                    riverDebrisList = riverDebrisList.filter { it != debris }
                                    if (riverDebrisList.isEmpty()) {
                                        isSuccess = true
                                        userFeedback = "Great job! The water is flowing crystal clear again.\nYou earned $totalPointsToReward EcoPoints for Sanskowik6.3!"
                                        gameState = GameState.Finished
                                    }
                                },
                                onCleanAll = {
                                    riverDebrisList = emptyList()
                                    isSuccess = true
                                    userFeedback = "Great job! The water is flowing crystal clear again.\nYou earned $totalPointsToReward EcoPoints for Sanskowik6.3!"
                                    gameState = GameState.Finished
                                }
                            )
                        }

                        EcoGameType.GHAT_GUARDIAN -> {
                            GhatGuardianGameContent(
                                onOptionSelected = { isEcoFriendly ->
                                    if (isEcoFriendly) {
                                        isSuccess = true
                                        userFeedback = "Excellent choice! Using traditional and eco-friendly methods preserves the sacred heritage and river ecosystem.\nYou earned $totalPointsToReward EcoPoints for Sanskowik6.3!"
                                        gameState = GameState.Finished
                                    } else {
                                        isSuccess = false
                                        userFeedback = "Chemicals harmed the local aquatic ecosystem! Always choose eco-friendly tools and natural baskets."
                                        gameState = GameState.Finished
                                    }
                                }
                            )
                        }

                        EcoGameType.ECO_SORT -> {
                            val currentQ = sortingQuestions[currentItemIndex % sortingQuestions.size]
                            EcoSortGameContent(
                                question = currentQ,
                                onAnswerSelected = { category ->
                                    if (category == currentQ.correctCategory) {
                                        isSuccess = true
                                        userFeedback = "Correct! ${currentQ.explanation}\nYou earned $totalPointsToReward EcoPoints for Sanskowik6.3!"
                                        gameState = GameState.Finished
                                    } else {
                                        isSuccess = false
                                        userFeedback = "Incorrect category for this waste. ${currentQ.explanation}"
                                        gameState = GameState.Finished
                                    }
                                }
                            )
                        }
                    }
                } else {
                    // Result State (Victory / Defeat)
                    ResultGameContent(
                        isSuccess = isSuccess,
                        message = userFeedback ?: "",
                        totalPoints = totalPointsToReward,
                        tokens = tokensToReward,
                        winStreak = winStreak,
                        onClaim = {
                            if (isSuccess) {
                                onGameCompleted(totalPointsToReward, tokensToReward, 25)
                            }
                            onDismiss()
                        },
                        onRetry = {
                            riverDebrisList = listOf(
                                "🍾 Plastic Bottle",
                                "🛍️ Polythene Bag",
                                "🥤 Styrofoam Cup",
                                "🥫 Discarded Can"
                            )
                            currentItemIndex++
                            gameState = GameState.Playing
                            isSuccess = false
                            userFeedback = null
                        }
                    )
                }
            }
        }
    }
}

private enum class GameState {
    Playing,
    Finished
}

private enum class WasteCategory {
    RECYCLABLE,
    ORGANIC
}

private data class SortingQuestion(
    val item: String,
    val correctCategory: WasteCategory,
    val explanation: String
)

@Composable
private fun RiverRescueGameContent(
    debrisList: List<String>,
    onDebrisRemoved: (String) -> Unit,
    onCleanAll: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "🌊 The river is clogged with plastic bottles and non-biodegradable waste!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Tap individual debris to scoop them out, or hit 'Clean & Restore Water Flow'!",
            style = MaterialTheme.typography.bodySmall,
            color = GangaTealDark,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        // River Animated Simulation Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFE0F7FA),
                            Color(0xFF80DEEA),
                            Color(0xFF26C6DA)
                        )
                    )
                )
                .border(2.dp, GangaTealPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (debrisList.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🐬 ✨ 🌊", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Pristine Crystal Clear Water!",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00695C),
                        fontSize = 14.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        debrisList.take(2).forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.9f),
                                shadowElevation = 2.dp,
                                modifier = Modifier
                                    .clickable { onDebrisRemoved(item) }
                                    .testTag("debris_${item.take(2)}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (debrisList.size > 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            debrisList.drop(2).forEach { item ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.9f),
                                    shadowElevation = 2.dp,
                                    modifier = Modifier
                                        .clickable { onDebrisRemoved(item) }
                                        .testTag("debris_${item.take(2)}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(item, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onCleanAll,
            colors = ButtonDefaults.buttonColors(containerColor = GangaTealPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("clean_river_button")
        ) {
            Icon(imageVector = Icons.Default.Water, contentDescription = "Clean River")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Type 'clean' / Instant River Sweep", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GhatGuardianGameContent(
    onOptionSelected: (isEcoFriendly: Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "🏛️ The ancient stone steps of the sacred ghat are littered with offerings and plastic.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Choose your restoration & cleaning tool:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = GangaTealDark
        )

        // Option 1: Eco-friendly broom & natural basket
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GangaTealContainer.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOptionSelected(true) }
                .testTag("ghat_option_eco")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🧹 🧺", fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "1. Eco-friendly broom & natural basket",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GangaTealDark
                    )
                    Text(
                        text = "Traditional sustainable tools preserving heritage stone.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Select",
                    tint = GangaTealDark
                )
            }
        }

        // Option 2: Plastic bag & chemical detergent
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOptionSelected(false) }
                .testTag("ghat_option_chemical")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🛍️ 🧪", fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "2. Plastic bag & chemical detergent",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Harsh synthetic solvents washing into the river flow.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Select",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EcoSortGameContent(
    question: SortingQuestion,
    onAnswerSelected: (WasteCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "♻️ Sort the collected waste correctly into the proper stream!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        // Item Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = GoldYellowContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Is this item Recyclable or Organic?",
                    fontSize = 12.sp,
                    color = OnGoldYellowContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = question.item,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnGoldYellowContainer
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onAnswerSelected(WasteCategory.RECYCLABLE) },
                colors = ButtonDefaults.buttonColors(containerColor = GangaTealPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("sort_recyclable_button")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("♻️", fontSize = 20.sp)
                    Text("Recyclable", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Button(
                onClick = { onAnswerSelected(WasteCategory.ORGANIC) },
                colors = ButtonDefaults.buttonColors(containerColor = EcoGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("sort_organic_button")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍂", fontSize = 20.sp)
                    Text("Organic", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun ResultGameContent(
    isSuccess: Boolean,
    message: String,
    totalPoints: Int,
    tokens: Int,
    winStreak: Int,
    onClaim: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isSuccess) GoldYellowContainer else MaterialTheme.colorScheme.errorContainer
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (isSuccess) "🎉" else "⚠️", fontSize = 32.sp)
        }

        Text(
            text = if (isSuccess) "Ganga Eco Victory!" else "Try Again!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSuccess) GangaTealDark else MaterialTheme.colorScheme.error
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (isSuccess) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = GoldYellowContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⭐ +$totalPoints", fontWeight = FontWeight.ExtraBold, color = GangaTealDark, fontSize = 16.sp)
                        Text("EcoPoints", fontSize = 11.sp, color = GangaTealDark.copy(alpha = 0.8f))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🪙 +$tokens", fontWeight = FontWeight.ExtraBold, color = OnGoldYellowContainer, fontSize = 16.sp)
                        Text("Eco-Tokens", fontSize = 11.sp, color = OnGoldYellowContainer.copy(alpha = 0.8f))
                    }
                    if (winStreak >= 1) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔥 ${winStreak + 1}x", fontWeight = FontWeight.ExtraBold, color = Color(0xFFD97706), fontSize = 16.sp)
                            Text("Streak Boost", fontSize = 11.sp, color = Color(0xFFD97706))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onClaim,
                colors = ButtonDefaults.buttonColors(containerColor = GoldYellowPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("claim_eco_reward_button")
            ) {
                Text("Claim EcoPoints & Tokens", fontWeight = FontWeight.Bold, color = OnGoldYellowContainer)
            }
        } else {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = GangaTealPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("retry_eco_game_button")
            ) {
                Text("Play Again", fontWeight = FontWeight.Bold)
            }
        }
    }
}
