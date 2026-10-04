package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: UserProfile)

    @Update
    suspend fun updateUserProfile(user: UserProfile)

    @Query("UPDATE user_profile SET tokens = tokens + :tokensToAdd, points = points + :pointsToAdd, totalCleanups = totalCleanups + 1, totalWasteCollectedKg = totalWasteCollectedKg + :wasteKg WHERE id = 1")
    suspend fun addRewardTokensAndPoints(tokensToAdd: Int, pointsToAdd: Int, wasteKg: Double)

    @Query("UPDATE user_profile SET tokens = tokens - :tokenCost WHERE id = 1 AND tokens >= :tokenCost")
    suspend fun deductTokens(tokenCost: Int): Int

    @Query("UPDATE user_profile SET isLoggedIn = :loggedIn WHERE id = 1")
    suspend fun setLoginStatus(loggedIn: Boolean)

    // Action Posts
    @Query("SELECT * FROM action_posts ORDER BY timestamp DESC")
    fun getAllActionPosts(): Flow<List<ActionPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActionPost(post: ActionPost)

    @Query("UPDATE action_posts SET likesCount = likesCount + (CASE WHEN isLikedByMe = 1 THEN -1 ELSE 1 END), isLikedByMe = (CASE WHEN isLikedByMe = 1 THEN 0 ELSE 1 END) WHERE id = :postId")
    suspend fun toggleLike(postId: Int)

    // Rewards
    @Query("SELECT * FROM reward_items")
    fun getAllRewards(): Flow<List<RewardItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewards(rewards: List<RewardItem>)

    @Query("UPDATE reward_items SET isRedeemed = 1, stockAvailable = stockAvailable - 1 WHERE id = :rewardId AND stockAvailable > 0")
    suspend fun markRewardRedeemed(rewardId: String)
}
