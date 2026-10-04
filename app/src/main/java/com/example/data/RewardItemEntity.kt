package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_items")
data class RewardItem(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val tokenCost: Int,
    val category: String,
    val emoji: String,
    val stockAvailable: Int,
    val isRedeemed: Boolean = false,
    val impactClaim: String
)

data class GangaHotspot(
    val id: String,
    val city: String,
    val ghatName: String,
    val waterQualityIndex: Int, // e.g. 72/100 (B+ Good)
    val status: String,
    val activeVolunteers: Int,
    val wasteRemovedThisMonthKg: Double,
    val isCleanAlert: Boolean
)
