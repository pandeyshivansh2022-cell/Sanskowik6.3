package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ActionCategory(val displayName: String, val icon: String) {
    PLASTIC_CLEANUP("Plastic & Ghat Cleanup", "♻️"),
    PUJA_WASTE_REMEDIATION("Puja & Flora Waste Remediation", "🌸"),
    SEWAGE_OUTFALL_REPORT("Sewage / Outfall Report", "⚠️"),
    RIVERBANK_PLANTATION("Riparian Tree Plantation", "🌱"),
    WATER_QUALITY_TESTING("Water Quality & WQI Check", "🧪"),
    COMMUNITY_DRIVE("Mass Community Drive", "👥")
}

enum class VerificationStatus(val label: String) {
    AI_VERIFIED("AI Impact Verified ✅"),
    COMMUNITY_CONFIRMED("Community Confirmed 🌟"),
    PENDING_REVIEW("Under Review ⏳")
}

@Entity(tableName = "action_posts")
data class ActionPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val location: String,
    val category: String, // ActionCategory name
    val beforeImageUrl: String, // drawable res name or uri string
    val afterImageUrl: String,  // drawable res name or uri string
    val videoUri: String? = null,
    val tokensEarned: Int,
    val pointsEarned: Int,
    val wasteCollectedKg: Double,
    val areaCleanedSqM: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val authorName: String = "Shivansh Pandey",
    val authorAvatar: String = "🌊",
    val verificationStatus: String = VerificationStatus.AI_VERIFIED.name,
    val likesCount: Int = 18,
    val isLikedByMe: Boolean = false,
    val impactNote: String = "Directly prevented plastic ingestion by river dolphins and improved Dissolved Oxygen along riverbank."
)
