package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfile?> = appDao.getUserProfile()
    val actionPosts: Flow<List<ActionPost>> = appDao.getAllActionPosts()
    val rewards: Flow<List<RewardItem>> = appDao.getAllRewards()

    suspend fun saveUserProfile(user: UserProfile) {
        appDao.insertUserProfile(user)
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        appDao.setLoginStatus(loggedIn)
    }

    suspend fun createActionPost(
        title: String,
        description: String,
        location: String,
        category: String,
        beforeImage: String,
        afterImage: String,
        videoUri: String?,
        wasteKg: Double,
        areaSqM: Double,
        authorName: String,
        authorAvatar: String
    ): ActionPost {
        // Calculate tokens & points dynamically based on real environmental impact!
        val baseTokens = (wasteKg * 2.0).toInt().coerceAtLeast(10)
        val areaBonusTokens = (areaSqM * 0.15).toInt()
        val totalTokens = (baseTokens + areaBonusTokens).coerceIn(15, 100)
        val totalPoints = totalTokens * 5

        val post = ActionPost(
            title = title,
            description = description,
            location = location,
            category = category,
            beforeImageUrl = beforeImage,
            afterImageUrl = afterImage,
            videoUri = videoUri,
            tokensEarned = totalTokens,
            pointsEarned = totalPoints,
            wasteCollectedKg = wasteKg,
            areaCleanedSqM = areaSqM,
            timestamp = System.currentTimeMillis(),
            authorName = authorName,
            authorAvatar = authorAvatar,
            verificationStatus = VerificationStatus.AI_VERIFIED.name,
            likesCount = 1,
            isLikedByMe = true,
            impactNote = "AI verified: Cleaned ${wasteKg}kg waste across ${areaSqM.toInt()}m² along River Ganga bank."
        )

        appDao.insertActionPost(post)
        appDao.addRewardTokensAndPoints(totalTokens, totalPoints, wasteKg)
        return post
    }

    suspend fun toggleLike(postId: Int) {
        appDao.toggleLike(postId)
    }

    suspend fun redeemReward(reward: RewardItem): Boolean {
        val rowsAffected = appDao.deductTokens(reward.tokenCost)
        if (rowsAffected > 0) {
            appDao.markRewardRedeemed(reward.id)
            return true
        }
        return false
    }

    fun getGangaHotspots(): List<GangaHotspot> {
        return listOf(
            GangaHotspot("hs_1", "Varanasi", "Assi & Dashashwamedh Ghats", 84, "Good (Class B)", 340, 1250.0, false),
            GangaHotspot("hs_2", "Haridwar", "Har Ki Pauri & Mansa Devi Ghat", 92, "Pristine (Class A)", 520, 890.0, false),
            GangaHotspot("hs_3", "Rishikesh", "Triveni & Parmarth Niketan Ghat", 96, "Crystal Pure (Class A+)", 410, 420.0, false),
            GangaHotspot("hs_4", "Prayagraj", "Triveni Sangam & Saraswati Ghat", 78, "Moderate Clean (Class B)", 280, 2100.0, true),
            GangaHotspot("hs_5", "Kanpur", "Bithoor & Parmat Ghat", 68, "Needs Attention (Class C)", 195, 3400.0, true),
            GangaHotspot("hs_6", "Patna", "Gandhi Ghat & Collectorate Ghat", 74, "Improving (Class B-)", 160, 1580.0, false),
            GangaHotspot("hs_7", "Kolkata", "Babu Ghat & Princep Ghat", 71, "Moderate (Class B)", 220, 1920.0, false)
        )
    }
}
