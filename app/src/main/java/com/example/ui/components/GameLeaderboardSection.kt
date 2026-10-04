package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GangaTealContainer
import com.example.ui.theme.GangaTealDark
import com.example.ui.theme.GoldYellowContainer
import com.example.ui.theme.GoldYellowPrimary

data class GameScoreEntry(
    val rank: Int,
    val playerName: String,
    val gameTitle: String,
    val score: Int,
    val avatar: String,
    val dateBadge: String
)

@Composable
fun GameLeaderboardSection(
    modifier: Modifier = Modifier
) {
    var selectedGameFilter by remember { mutableStateOf("All") }

    val allScores = listOf(
        GameScoreEntry(1, "Aarav Sharma", "Strategy War", 2950, "🛡️", "Supreme Commander"),
        GameScoreEntry(2, "Priya Patel", "Action RPG", 2720, "⚔️", "Grand Paladin"),
        GameScoreEntry(3, "Rohan Verma", "Strategy War", 2350, "🛡️", "Tactician"),
        GameScoreEntry(4, "Ananya Gupta", "Action RPG", 2140, "⚔️", "Hero of Ganga"),
        GameScoreEntry(5, "Vikram Singh", "Strategy War", 1980, "🛡️", "Warlord"),
        GameScoreEntry(6, "Neha Sharma", "Action RPG", 1850, "⚔️", "Guardian Blade"),
        GameScoreEntry(7, "Amit Kumar", "Strategy War", 1720, "🛡️", "Strategist"),
        GameScoreEntry(8, "Sneha Rao", "Action RPG", 1640, "⚔️", "Eco Warrior")
    )

    val filteredScores = if (selectedGameFilter == "All") {
        allScores
    } else {
        allScores.filter { it.gameTitle == selectedGameFilter }
    }

    val categories = listOf("All", "Action RPG", "Strategy War")

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("game_leaderboard_section")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(GoldYellowContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Leaderboard Trophy",
                            tint = GoldYellowPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Game Zone Leaderboard",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Top scorers across Action RPG & Strategy War",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Game Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedGameFilter == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedGameFilter = category },
                        label = { Text(category, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GangaTealContainer,
                            selectedLabelColor = GangaTealDark
                        )
                    )
                }
            }

            // Scores List
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                filteredScores.forEachIndexed { index, entry ->
                    val rank = index + 1
                    val isTopThree = rank <= 3

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Rank Badge
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            when (rank) {
                                                1 -> GoldYellowPrimary
                                                2 -> Color(0xFF94A3B8)
                                                3 -> Color(0xFFD97706)
                                                else -> MaterialTheme.colorScheme.surfaceVariant
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#$rank",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isTopThree) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(text = entry.avatar, fontSize = 20.sp)

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = entry.playerName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${entry.gameTitle} • ${entry.dateBadge}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Score display
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GangaTealContainer
                            ) {
                                Text(
                                    text = "${entry.score} pts",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GangaTealDark,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
