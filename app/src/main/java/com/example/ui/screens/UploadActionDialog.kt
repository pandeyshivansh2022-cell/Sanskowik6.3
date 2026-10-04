package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.data.ActionCategory
import com.example.ui.theme.EcoGreen
import com.example.ui.theme.GangaTealContainer
import com.example.ui.theme.GangaTealDark
import com.example.ui.theme.GangaTealPrimary
import com.example.ui.theme.GoldYellowContainer
import com.example.ui.theme.GoldYellowDark
import com.example.ui.theme.GoldYellowLight
import com.example.ui.theme.GoldYellowPrimary
import com.example.ui.theme.OnGoldYellowContainer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UploadActionDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        description: String,
        location: String,
        category: ActionCategory,
        beforeImage: String,
        afterImage: String,
        videoUri: String?,
        wasteKg: Double,
        areaSqM: Double
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ActionCategory.PLASTIC_CLEANUP) }

    val gangaGhatLocations = remember {
        listOf(
            "Dashashwamedh Ghat, Varanasi, UP",
            "Assi Ghat, Varanasi, UP",
            "Har Ki Pauri, Haridwar, UK",
            "Triveni Ghat, Rishikesh, UK",
            "Triveni Sangam, Prayagraj, UP",
            "Gandhi Ghat, Patna, Bihar",
            "Babu Ghat, Kolkata, West Bengal",
            "Parmat Ghat, Kanpur, UP"
        )
    }

    var selectedLocation by remember { mutableStateOf(gangaGhatLocations[0]) }
    var locationExpanded by remember { mutableStateOf(false) }

    // Image references
    var beforeImageRef by remember { mutableStateOf("ganga_before_clean") }
    var afterImageRef by remember { mutableStateOf("ganga_after_clean") }
    var videoAttached by remember { mutableStateOf(false) }

    var wasteKg by remember { mutableDoubleStateOf(15.0) }
    var areaSqM by remember { mutableDoubleStateOf(80.0) }

    // Dynamic Token and Points Calculator
    val estimatedTokens = remember(wasteKg, areaSqM) {
        val base = (wasteKg * 2.0).toInt().coerceAtLeast(10)
        val area = (areaSqM * 0.15).toInt()
        (base + area).coerceIn(15, 100)
    }
    val estimatedPoints = estimatedTokens * 5

    // Image Pickers
    val beforePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            beforeImageRef = uri.toString()
        }
    }

    val afterPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            afterImageRef = uri.toString()
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            videoAttached = true
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxSize(0.92f)
                .testTag("upload_action_dialog")
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Dialog Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GoldYellowContainer)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GoldYellowPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Eco Action",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Document Ganga Action",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = OnGoldYellowContainer
                            )
                            Text(
                                text = "Upload before/after photos & earn tokens",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnGoldYellowContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_upload_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = OnGoldYellowContainer
                        )
                    }
                }

                // Scrollable Form Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Estimated Reward Highlight Banner
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GangaTealContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Impact AI",
                                    tint = GangaTealDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Estimated AI Verified Reward",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GangaTealDark
                                    )
                                    Text(
                                        text = "Based on waste cleared & area remediated",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GangaTealDark.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "+$estimatedTokens 🪙",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = OnGoldYellowContainer
                                )
                                Text(
                                    text = "+$estimatedPoints Pts ⭐",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GangaTealDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Action Title *") },
                        placeholder = { Text("e.g., Dashashwamedh Ghat Plastic Sweep") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_title_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Location Dropdown
                    ExposedDropdownMenuBox(
                        expanded = locationExpanded,
                        onExpandedChange = { locationExpanded = !locationExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedLocation,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("River Ganga Location / Ghat *") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location", tint = GoldYellowDark)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("location_dropdown"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = locationExpanded,
                            onDismissRequest = { locationExpanded = false }
                        ) {
                            gangaGhatLocations.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc) },
                                    onClick = {
                                        selectedLocation = loc
                                        locationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Category Selection Chips
                    Text(
                        text = "Environmental Action Category",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ActionCategory.values().forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text("${cat.icon} ${cat.displayName}", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldYellowContainer,
                                    selectedLabelColor = OnGoldYellowContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Before & After Visual Upload Cards
                    Text(
                        text = "Photo Proof (Before & After Visuals) *",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Upload before and after photos to substantiate your pollution reduction impact.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // BEFORE PHOTO BOX
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(140.dp)
                                .clickable { beforePickerLauncher.launch("image/*") }
                                .testTag("upload_before_photo_button")
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (beforeImageRef == "ganga_before_clean") {
                                    Image(
                                        painter = painterResource(id = R.drawable.ganga_before_clean),
                                        contentDescription = "Before Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    AsyncImage(
                                        model = beforeImageRef,
                                        contentDescription = "Before Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Surface(
                                    color = Color(0xFFDC2626).copy(alpha = 0.85f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(6.dp)
                                ) {
                                    Text(
                                        text = "🔴 BEFORE",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Change before photo",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(16.dp)
                                    )
                                }
                            }
                        }

                        // AFTER PHOTO BOX
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(140.dp)
                                .clickable { afterPickerLauncher.launch("image/*") }
                                .testTag("upload_after_photo_button")
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (afterImageRef == "ganga_after_clean") {
                                    Image(
                                        painter = painterResource(id = R.drawable.ganga_after_clean),
                                        contentDescription = "After Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    AsyncImage(
                                        model = afterImageRef,
                                        contentDescription = "After Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Surface(
                                    color = GangaTealPrimary.copy(alpha = 0.85f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(6.dp)
                                ) {
                                    Text(
                                        text = "🟢 AFTER",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Change after photo",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Optional Video Upload Box
                    OutlinedButton(
                        onClick = {
                            videoPickerLauncher.launch("video/*")
                            videoAttached = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_video_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (videoAttached) Icons.Default.CheckCircle else Icons.Default.Videocam,
                            contentDescription = "Video",
                            tint = if (videoAttached) EcoGreen else GangaTealPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (videoAttached) "Video Proof Attached (ganga_action_log.mp4)" else "Attach Optional Video Proof / Drone Footage",
                            color = if (videoAttached) EcoGreen else GangaTealDark,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Waste Collected Slider (kg)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estimated Waste Diverted (kg)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = GoldYellowContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${"%.1f".format(wasteKg)} kg",
                                fontWeight = FontWeight.Bold,
                                color = OnGoldYellowContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Slider(
                        value = wasteKg.toFloat(),
                        onValueChange = { wasteKg = it.toDouble() },
                        valueRange = 2f..80f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldYellowPrimary,
                            activeTrackColor = GoldYellowPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("waste_slider")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Area Cleaned Slider (sq meters)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Riverbank Area Cleaned (m²)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = GangaTealContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${areaSqM.toInt()} m²",
                                fontWeight = FontWeight.Bold,
                                color = GangaTealDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Slider(
                        value = areaSqM.toFloat(),
                        onValueChange = { areaSqM = it.toDouble() },
                        valueRange = 10f..300f,
                        colors = SliderDefaults.colors(
                            thumbColor = GangaTealPrimary,
                            activeTrackColor = GangaTealPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("area_slider")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Impact Notes & Volunteer Details") },
                        placeholder = { Text("Describe items collected, challenges overcome, and segregation process...") },
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_description_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Bottom Submit Section
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("cancel_upload_button")
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                val finalTitle = if (title.isNotBlank()) title else "${selectedCategory.displayName} at ${selectedLocation.substringBefore(",")}"
                                val finalDesc = if (description.isNotBlank()) description else "Successfully remediated pollution along ${selectedLocation}. Diverted ${"%.1f".format(wasteKg)}kg of river waste."
                                onSubmit(
                                    finalTitle,
                                    finalDesc,
                                    selectedLocation,
                                    selectedCategory,
                                    beforeImageRef,
                                    afterImageRef,
                                    if (videoAttached) "content://sample_video" else null,
                                    wasteKg,
                                    areaSqM
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldYellowPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("submit_action_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = "Submit",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Submit & Earn +$estimatedTokens 🪙",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
