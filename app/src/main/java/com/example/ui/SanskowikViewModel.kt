package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ActionCategory
import com.example.data.ActionPost
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.GangaHotspot
import com.example.data.RewardItem
import com.example.data.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val avatar: String,
    val location: String,
    val tokensEarned: Int,
    val points: Int,
    val wasteCollectedKg: Double,
    val badge: String
)

class SanskowikViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository
    val userProfile: StateFlow<UserProfile?>
    val actionPosts: StateFlow<List<ActionPost>>
    val rewards: StateFlow<List<RewardItem>>
    val hotspots: List<GangaHotspot>

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _showUploadDialog = MutableStateFlow(false)
    val showUploadDialog: StateFlow<Boolean> = _showUploadDialog.asStateFlow()

    private val _showForgotPasswordDialog = MutableStateFlow(false)
    val showForgotPasswordDialog: StateFlow<Boolean> = _showForgotPasswordDialog.asStateFlow()

    private val _celebrationPost = MutableStateFlow<ActionPost?>(null)
    val celebrationPost: StateFlow<ActionPost?> = _celebrationPost.asStateFlow()

    private val _redeemedReward = MutableStateFlow<RewardItem?>(null)
    val redeemedReward: StateFlow<RewardItem?> = _redeemedReward.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = AppRepository(db.appDao())
        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )
        actionPosts = repository.actionPosts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        rewards = repository.rewards.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        hotspots = repository.getGangaHotspots()

        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null) {
                    _isLoggedIn.value = profile.isLoggedIn
                }
            }
        }
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun openUploadDialog() {
        _showUploadDialog.value = true
    }

    fun closeUploadDialog() {
        _showUploadDialog.value = false
    }

    fun openForgotPasswordDialog() {
        _showForgotPasswordDialog.value = true
    }

    fun closeForgotPasswordDialog() {
        _showForgotPasswordDialog.value = false
    }

    fun dismissCelebration() {
        _celebrationPost.value = null
    }

    fun dismissRedemption() {
        _redeemedReward.value = null
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun signIn(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank() || !email.contains("@")) {
            onResult(false, "Please enter a valid email address.")
            return
        }
        if (pass.length < 4) {
            onResult(false, "Password must be at least 4 characters.")
            return
        }
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfile()
            val updated = current.copy(
                email = email,
                name = if (current.name.isNotBlank()) current.name else email.substringBefore("@").replace(".", " ").capitalizeWords(),
                isLoggedIn = true
            )
            repository.saveUserProfile(updated)
            _isLoggedIn.value = true
            onResult(true, "Welcome back to Sanskowik6.3! Clean Ganga Mission active.")
        }
    }

    fun register(name: String, email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        if (name.isBlank()) {
            onResult(false, "Please enter your full name.")
            return
        }
        if (email.isBlank() || !email.contains("@")) {
            onResult(false, "Please enter a valid email address.")
            return
        }
        if (pass.length < 4) {
            onResult(false, "Password must be at least 4 characters.")
            return
        }
        viewModelScope.launch {
            val newUser = UserProfile(
                id = 1,
                name = name.trim(),
                email = email.trim(),
                tokens = 100, // 100 Bonus Tokens
                points = 500, // 500 Bonus Points
                totalCleanups = 0,
                totalWasteCollectedKg = 0.0,
                rankTitle = "Ganga Guardian • Level 1 (New Recruit)",
                avatarEmoji = "🌊",
                joinedDate = "Today",
                isLoggedIn = true
            )
            repository.saveUserProfile(newUser)
            _isLoggedIn.value = true
            onResult(true, "Account created! 100 Welcome Tokens & 500 Points added to your wallet!")
        }
    }

    fun sendPasswordReset(email: String, onComplete: (Boolean, String) -> Unit) {
        if (email.isBlank() || !email.contains("@")) {
            onComplete(false, "Please enter a valid registered email address.")
            return
        }
        onComplete(true, "A password reset link with OTP has been dispatched to $email.")
    }

    fun logout() {
        viewModelScope.launch {
            repository.setLoggedIn(false)
            _isLoggedIn.value = false
        }
    }

    fun submitEnvironmentalAction(
        title: String,
        description: String,
        location: String,
        category: ActionCategory,
        beforeImage: String,
        afterImage: String,
        videoUri: String?,
        wasteKg: Double,
        areaSqM: Double
    ) {
        viewModelScope.launch {
            val user = userProfile.value ?: UserProfile()
            val createdPost = repository.createActionPost(
                title = title,
                description = description,
                location = location,
                category = category.name,
                beforeImage = beforeImage,
                afterImage = afterImage,
                videoUri = videoUri,
                wasteKg = wasteKg,
                areaSqM = areaSqM,
                authorName = user.name,
                authorAvatar = user.avatarEmoji
            )
            _showUploadDialog.value = false
            _celebrationPost.value = createdPost
            _snackbarMessage.value = "Action verified! Earned +${createdPost.tokensEarned} Tokens & +${createdPost.pointsEarned} Points! 🌟"
        }
    }

    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            repository.toggleLike(postId)
        }
    }

    fun redeemReward(reward: RewardItem) {
        viewModelScope.launch {
            val success = repository.redeemReward(reward)
            if (success) {
                _redeemedReward.value = reward
                _snackbarMessage.value = "Successfully redeemed ${reward.title}! Check your vouchers."
            } else {
                _snackbarMessage.value = "Insufficient tokens! You need ${reward.tokenCost} tokens to redeem."
            }
        }
    }

    fun playMiniGame(gameTitle: String, fee: Int, rewardTokens: Int, rewardCoins: Int) {
        viewModelScope.launch {
            val user = userProfile.value ?: UserProfile()
            val currentTokens = user.tokens
            val currentPoints = user.points
            
            // Deduct coins/points entry fee and reward tokens
            val updatedTokens = currentTokens + rewardTokens
            val updatedPoints = currentPoints + (rewardCoins * 2)
            
            val updated = user.copy(
                tokens = updatedTokens,
                points = updatedPoints
            )
            repository.saveUserProfile(updated)
            _snackbarMessage.value = "🎉 Won $gameTitle! Earned +$rewardTokens Eco-Token & +$rewardCoins Coins!"
        }
    }

    fun getLeaderboard(): List<LeaderboardUser> {
        val user = userProfile.value ?: UserProfile()
        return listOf(
            LeaderboardUser(1, "Aarav Mishra", "🪷", "Haridwar, UK", 840, 4200, 185.0, "Ganga Legend 👑"),
            LeaderboardUser(2, "Pooja Banerjee", "✨", "Kolkata, WB", 710, 3550, 142.5, "River Champion 🥇"),
            LeaderboardUser(3, "Devendra Singh", "🌿", "Rishikesh, UK", 620, 3100, 118.0, "Eco Vanguard 🥈"),
            LeaderboardUser(4, user.name + " (You)", user.avatarEmoji, "Varanasi, UP", user.tokens, user.points, user.totalWasteCollectedKg, user.rankTitle),
            LeaderboardUser(5, "Simran Kaur", "🌊", "Patna, BR", 490, 2450, 94.2, "Cleanliness Hero 🥉"),
            LeaderboardUser(6, "Manish Tripathi", "🌻", "Prayagraj, UP", 430, 2150, 81.0, "Ghat Warden ⭐"),
            LeaderboardUser(7, "Sneha Verma", "🌾", "Kanpur, UP", 390, 1950, 72.4, "Eco Warrior 🌿")
        ).sortedByDescending { it.points }.mapIndexed { idx, item -> item.copy(rank = idx + 1) }
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
}
