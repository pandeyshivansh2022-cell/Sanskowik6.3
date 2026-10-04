package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.ActionPost
import com.example.ui.theme.EcoGreen
import com.example.ui.theme.EcoGreenLight
import com.example.ui.theme.GangaTealContainer
import com.example.ui.theme.GangaTealDark
import com.example.ui.theme.GangaTealPrimary
import com.example.ui.theme.GoldYellowContainer
import com.example.ui.theme.GoldYellowDark
import com.example.ui.theme.GoldYellowLight
import com.example.ui.theme.GoldYellowPrimary
import com.example.ui.theme.OnGoldYellowContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BeforeAfterVisualCard(
    post: ActionPost,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var viewMode by remember { mutableIntStateOf(0) } // 0: Interactive Slider, 1: Side-by-Side, 2: After Only, 3: Before Only
    var sliderProgress by remember { mutableFloatStateOf(0.5f) }
    var showVideoDialog by remember { mutableStateOf(false) }

    val formattedTime = remember(post.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
        sdf.format(Date(post.timestamp))
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("action_post_card_${post.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Author & Status Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GoldYellowContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorAvatar,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Action",
                            tint = GangaTealPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = GoldYellowDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = post.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // AI Verification Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EcoGreenLight,
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Impact",
                            tint = Color(0xFF065F46),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Verified",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46)
                        )
                    }
                }
            }

            // Title & Description
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Visual Mode Tabs (Interactive Slider / Split / Before / After)
            TabRow(
                selectedTabIndex = viewMode,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = GoldYellowDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = viewMode == 0,
                    onClick = { viewMode = 0 },
                    text = { Text("Interactive Slider", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = viewMode == 1,
                    onClick = { viewMode = 1 },
                    text = { Text("Side-by-Side", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = viewMode == 2,
                    onClick = { viewMode = 2 },
                    text = { Text("After Clean", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = viewMode == 3,
                    onClick = { viewMode = 3 },
                    text = { Text("Before", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Visual Display Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black)
            ) {
                when (viewMode) {
                    0 -> {
                        // Interactive Drag Slider Between Before and After
                        InteractiveSliderView(
                            beforeImage = post.beforeImageUrl,
                            afterImage = post.afterImageUrl,
                            sliderProgress = sliderProgress,
                            onProgressChange = { sliderProgress = it }
                        )
                    }
                    1 -> {
                        // Side-by-side split
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                GangaSmartImage(
                                    imageRef = post.beforeImageUrl,
                                    contentDescription = "Before cleanup",
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "🔴 BEFORE",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxSize()
                                    .background(Color.White)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                GangaSmartImage(
                                    imageRef = post.afterImageUrl,
                                    contentDescription = "After cleanup",
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    color = GangaTealPrimary.copy(alpha = 0.85f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "🟢 AFTER",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                    2 -> {
                        // After Clean Full
                        Box(modifier = Modifier.fillMaxSize()) {
                            GangaSmartImage(
                                imageRef = post.afterImageUrl,
                                contentDescription = "After cleanup",
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                color = GangaTealPrimary.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "✨ AFTER CLEANUP (SPARKLING GANGA)",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    3 -> {
                        // Before Clean Full
                        Box(modifier = Modifier.fillMaxSize()) {
                            GangaSmartImage(
                                imageRef = post.beforeImageUrl,
                                contentDescription = "Before cleanup",
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                color = Color(0xFFDC2626).copy(alpha = 0.9f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "⚠️ BEFORE REMEDIATION (POLLUTED)",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Video Badge if attached
                if (post.videoUri != null) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clickable { showVideoDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleFilled,
                                contentDescription = "Video Proof",
                                tint = GoldYellowLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Video Log",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Interactive slider bar helper (only in slider mode)
            if (viewMode == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔴 Before",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = sliderProgress,
                        onValueChange = { sliderProgress = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .testTag("before_after_slider_${post.id}"),
                        colors = SliderDefaults.colors(
                            thumbColor = GoldYellowPrimary,
                            activeTrackColor = GangaTealPrimary,
                            inactiveTrackColor = Color(0xFFDC2626).copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = "🟢 After",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Impact metrics & reward breakdown
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tokens Earned
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🪙", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "+${post.tokensEarned} Tokens",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = OnGoldYellowContainer
                            )
                            Text(
                                text = "Earned",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Points Earned
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "+${post.pointsEarned} Pts",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = GangaTealDark
                            )
                            Text(
                                text = "Clean Karma",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Waste collected
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🗑️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "${post.wasteCollectedKg} kg",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Waste Diverted",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Impact note
            Text(
                text = post.impactNote,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Action Card Footer (Likes, Share, Timestamp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onLikeClick,
                        modifier = Modifier.testTag("like_button_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like Post",
                            tint = if (post.isLikedByMe) Color(0xFFE11D48) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${post.likesCount} Ganga Guardians applauded",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }

    if (showVideoDialog) {
        VideoProofDialog(
            post = post,
            onDismiss = { showVideoDialog = false }
        )
    }
}

@Composable
fun VideoProofDialog(
    post: ActionPost,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var playbackProgress by remember { mutableFloatStateOf(0.42f) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("video_proof_dialog")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Video Screen Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color.Black)
                ) {
                    GangaSmartImage(
                        imageRef = post.afterImageUrl,
                        contentDescription = "Video preview",
                        modifier = Modifier.fillMaxSize()
                    )

                    // Video overlay dark tint
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f))
                    )

                    // Telemetry Header
                    Surface(
                        color = Color.Black.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "🔴 REC • 4K UHD 60FPS • GPS: ${post.location}",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    // Play / Pause central button
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(56.dp)
                            .background(GoldYellowPrimary.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.PlayCircleFilled else Icons.Default.Videocam,
                            contentDescription = "Playback state",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Bottom progress timeline
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                )
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Slider(
                            value = playbackProgress,
                            onValueChange = { playbackProgress = it },
                            colors = SliderDefaults.colors(
                                thumbColor = GoldYellowPrimary,
                                activeTrackColor = GoldYellowPrimary,
                                inactiveTrackColor = Color.White.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "00:18",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "00:45",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // AI Video Verification Info
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Verified",
                            tint = GangaTealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Multi-Frame Verification Passed",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GangaTealDark
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Video footage confirms removal of debris along the riverbank edge with zero residual dumping.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    androidx.compose.material3.Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldYellowPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close Video Log", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveSliderView(
    beforeImage: String,
    afterImage: String,
    sliderProgress: Float,
    onProgressChange: (Float) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = maxWidth
        val height = maxHeight

        // After image is in background
        GangaSmartImage(
            imageRef = afterImage,
            contentDescription = "After Clean Ganga",
            modifier = Modifier.fillMaxSize()
        )

        // Clip before image according to progress
        val splitPx = width * sliderProgress

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(0.dp))
        ) {
            Box(
                modifier = Modifier
                    .width(splitPx)
                    .fillMaxSize()
            ) {
                GangaSmartImage(
                    imageRef = beforeImage,
                    contentDescription = "Before Cleanup",
                    modifier = Modifier
                        .fillMaxSize()
                        .width(width) // keep full aspect
                )
            }
        }

        // Divider line & handle
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxSize()
                .offset { IntOffset(x = (splitPx.toPx()).toInt() - 2, y = 0) }
                .background(Color.White)
        )

        // Floating drag badge
        Box(
            modifier = Modifier
                .size(36.dp)
                .offset { IntOffset(x = (splitPx.toPx()).toInt() - 18, y = (height.toPx() / 2).toInt() - 18) }
                .clip(CircleShape)
                .background(GoldYellowPrimary)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Compare,
                contentDescription = "Slide Compare",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Overlay labels
        Surface(
            color = Color.Black.copy(alpha = 0.7f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = "🔴 BEFORE CLEANUP",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }

        Surface(
            color = GangaTealPrimary.copy(alpha = 0.85f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Text(
                text = "🟢 AFTER CLEANUP",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
fun GangaSmartImage(
    imageRef: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    when {
        imageRef == "ganga_before_clean" -> {
            Image(
                painter = painterResource(id = R.drawable.ganga_before_clean),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        imageRef == "ganga_after_clean" -> {
            Image(
                painter = painterResource(id = R.drawable.ganga_after_clean),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        imageRef == "ganga_hero_banner" -> {
            Image(
                painter = painterResource(id = R.drawable.ganga_hero_banner),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        imageRef.startsWith("content://") || imageRef.startsWith("file://") || imageRef.startsWith("http") -> {
            AsyncImage(
                model = imageRef,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        else -> {
            // Fallback to sample after clean
            Image(
                painter = painterResource(id = R.drawable.ganga_after_clean),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    }
}
