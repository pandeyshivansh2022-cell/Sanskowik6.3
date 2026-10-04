package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Shivansh Pandey",
    val email: String = "pandey.shivansh2022@gmail.com",
    val tokens: Int = 100, // Welcome bonus of 100 tokens
    val points: Int = 500, // Welcome bonus of 500 points
    val totalCleanups: Int = 3,
    val totalWasteCollectedKg: Double = 36.5,
    val rankTitle: String = "Ganga Guardian • Level 2",
    val avatarEmoji: String = "🌊",
    val joinedDate: String = "August 2026",
    val isLoggedIn: Boolean = false
)
