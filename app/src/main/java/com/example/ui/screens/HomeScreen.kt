package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ActionPost
import com.example.data.GangaHotspot
import com.example.data.RewardItem
import com.example.data.UserProfile
import com.example.ui.LeaderboardUser
import com.example.ui.theme.EcoGreen
import com.example.ui.theme.GangaTealContainer
import com.example.ui.theme.GangaTealDark
import com.example.ui.theme.GangaTealLight
import com.example.ui.theme.GangaTealPrimary
import com.example.ui.theme.GoldYellowContainer
import com.example.ui.theme.GoldYellowDark
import com.example.ui.theme.GoldYellowLight
import com.example.ui.theme.GoldYellowPrimary
import com.example.ui.theme.OnGoldYellowContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: UserProfile,
    actionPosts: List<ActionPost>,
    rewards: List<RewardItem>,
    hotspots: List<GangaHotspot>,
    leaderboard: List<LeaderboardUser>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onLikeClick: (Int) -> Unit,
    onRedeemReward: (RewardItem) -> Unit,
    onPlayGame: (String, Int, Int, Int) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.sanskowik_logo_1786818240056),
                            contentDescription = "Sanskowik6.3 Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Sanskowik6.3",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "River Ganga Rejuvenation",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // User Live Token & Points Capsule
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = GoldYellowContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldYellowPrimary.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clickable { onTabSelected(1) }
                            .testTag("user_tokens_capsule")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🪙 ${user.tokens}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = OnGoldYellowContainer
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "⭐ ${user.points}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = GangaTealDark
                            )
                        }
                    }

                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Sign Out",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onUploadClick,
                containerColor = GoldYellowPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("upload_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Upload Action"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Document Action 📸",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
                    label = { Text("Actions") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnGoldYellowContainer,
                        selectedTextColor = OnGoldYellowContainer,
                        indicatorColor = GoldYellowContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_feed")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    icon = { Icon(Icons.Default.Eco, contentDescription = "Eco-Impact") },
                    label = { Text("Impact") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GangaTealDark,
                        selectedTextColor = GangaTealDark,
                        indicatorColor = GangaTealContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_impact")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { onTabSelected(2) },
                    icon = { Icon(Icons.Default.Water, contentDescription = "Hotspots") },
                    label = { Text("Ghats") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GangaTealDark,
                        selectedTextColor = GangaTealDark,
                        indicatorColor = GangaTealContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_hotspots")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { onTabSelected(3) },
                    icon = { Icon(Icons.Default.CardGiftcard, contentDescription = "Rewards") },
                    label = { Text("Rewards") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnGoldYellowContainer,
                        selectedTextColor = OnGoldYellowContainer,
                        indicatorColor = GoldYellowContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_rewards")
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { onTabSelected(4) },
                    icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Game Zone") },
                    label = { Text("Game Zone") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnGoldYellowContainer,
                        selectedTextColor = OnGoldYellowContainer,
                        indicatorColor = GoldYellowContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_gamezone")
                )
                NavigationBarItem(
                    selected = selectedTab == 5,
                    onClick = { onTabSelected(5) },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard") },
                    label = { Text("Rankings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OnGoldYellowContainer,
                        selectedTextColor = OnGoldYellowContainer,
                        indicatorColor = GoldYellowContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_leaderboard")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    // Feed & User Profile View
                    ActionsFeedWithProfile(
                        user = user,
                        posts = actionPosts,
                        onLikeClick = onLikeClick,
                        onUploadClick = onUploadClick,
                        onExploreMarketplace = { onTabSelected(3) },
                        onExploreImpact = { onTabSelected(1) }
                    )
                }
                1 -> {
                    // Dedicated Eco-Impact & Token Screen
                    EcoImpactScreen(
                        user = user,
                        onRedeemTokens = { onTabSelected(3) },
                        onDocumentAction = onUploadClick
                    )
                }
                2 -> {
                    // Ghats & Hotspots
                    HotspotsView(
                        hotspots = hotspots,
                        onPlanActionAtGhat = { onUploadClick() }
                    )
                }
                3 -> {
                    // Rewards Marketplace
                    MarketplaceView(
                        user = user,
                        rewards = rewards,
                        onRedeem = onRedeemReward
                    )
                }
                4 -> {
                    // Game Zone Desk
                    GameZoneScreen(
                        user = user,
                        onPlayGame = onPlayGame
                    )
                }
                5 -> {
                    // Leaderboard
                    LeaderboardView(users = leaderboard)
                }
            }
        }
    }
}

@Composable
fun ActionsFeedWithProfile(
    user: UserProfile,
    posts: List<ActionPost>,
    onLikeClick: (Int) -> Unit,
    onUploadClick: () -> Unit,
    onExploreMarketplace: () -> Unit,
    onExploreImpact: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // User Profile & Welcome Bonus Highlight Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // User Header with Avatar & Greeting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(GoldYellowLight, GoldYellowPrimary))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.avatarEmoji,
                                fontSize = 28.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Welcome, ${user.name}! 🌟",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MilitaryTech,
                                    contentDescription = "Rank",
                                    tint = GoldYellowDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user.rankTitle,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldYellowDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Initial Bonus Banner Prompt Requirement:
                    // "The homepage reveals a user profile, welcoming new users with an initial bonus of 100 tokens and 500 points."
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GoldYellowContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldYellowPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Eco,
                                        contentDescription = "Bonus",
                                        tint = GoldYellowDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Initial Welcome Bonus",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = OnGoldYellowContainer
                                    )
                                }
                                Text(
                                    text = "🪙 100 Eco-Tokens + ⭐ 500 Points Credited",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnGoldYellowContainer.copy(alpha = 0.85f)
                                )
                            }

                            Button(
                                onClick = onExploreMarketplace,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldYellowPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("redeem_tokens_banner_button")
                            ) {
                                Text("Redeem", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // User Stats Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Tokens Balance (Clickable to Eco-Impact Screen)
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExploreImpact() }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🪙 Tokens",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${user.tokens}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = OnGoldYellowContainer
                                )
                            }
                        }

                        // Karma Points (Clickable to Eco-Impact Screen)
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onExploreImpact() }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "⭐ Points",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${user.points}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = GangaTealDark
                                )
                            }
                        }

                        // Waste Cleared (kg)
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🗑️ Cleared",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${"%.1f".format(user.totalWasteCollectedKg)}kg",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mini Contribution Impact Score Progress Bar
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = GangaTealContainer.copy(alpha = 0.4f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onExploreImpact() }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Contribution Impact Score",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GangaTealDark
                                )
                                Text(
                                    text = "View Breakdown →",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GangaTealDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            androidx.compose.material3.LinearProgressIndicator(
                                progress = { (user.points.toFloat() / 1000f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = GangaTealPrimary,
                                trackColor = GangaTealContainer
                            )
                        }
                    }
                }
            }
        }

        // River Ganga Mission Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ganga_hero_banner),
                        contentDescription = "Ganga Rejuvenation Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.75f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Save Sacred River Ganga",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Upload photos & videos of your cleanup actions.\nEarn tokens, climb the leaderboard, redeem eco gifts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Environmental Action Feed Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ganga Cleanup Actions & Visuals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Interactive Before-and-After documentation",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onUploadClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldYellowPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("feed_upload_action_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Upload", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Action Posts List with Detailed Before/After Visuals
        items(posts, key = { it.id }) { post ->
            BeforeAfterVisualCard(
                post = post,
                onLikeClick = { onLikeClick(post.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
