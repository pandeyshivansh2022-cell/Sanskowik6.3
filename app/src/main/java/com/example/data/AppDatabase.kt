package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserProfile::class, ActionPost::class, RewardItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sanskowik_ganga_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.appDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        syncUpdatedRewards(database.appDao())
                    }
                }
            }

            suspend fun syncUpdatedRewards(dao: AppDao) {
                val updatedRewards = listOf(
                    RewardItem(
                        id = "rew_scholarship",
                        title = "Student Higher Education Scholarship Grant",
                        description = "Direct application voucher & tuition assistance grant co-sponsored by educational foundations for active youth volunteers.",
                        tokenCost = 1500, // 10x increased cost
                        category = "Scholarship",
                        emoji = "🎓",
                        stockAvailable = 20,
                        impactClaim = "Financial assistance for higher education and reduced student debt"
                    ),
                    RewardItem(
                        id = "rew_coaching",
                        title = "Competitive Exam & Coaching Center Pass",
                        description = "Discounted access to top-tier test preparation, tech bootcamps, and 1-on-1 expert faculty guidance sessions.",
                        tokenCost = 1200, // 10x increased cost
                        category = "Coaching",
                        emoji = "📚",
                        stockAvailable = 35,
                        impactClaim = "Enhanced employability and professional skill acquisition"
                    ),
                    RewardItem(
                        id = "rew_lifestyle",
                        title = "Local Heritage Dining & Cafe 50% Off Voucher",
                        description = "Exclusive discounts and special offers at popular local restaurants, cafes, and home decor boutiques in Varanasi.",
                        tokenCost = 800, // 10x increased cost
                        category = "Lifestyle & Dining",
                        emoji = "☕",
                        stockAvailable = 50,
                        impactClaim = "Everyday savings and support for local businesses"
                    ),
                    RewardItem(
                        id = "rew_mentorship",
                        title = "1-on-1 Startup & Business Growth Mentorship",
                        description = "Exclusive 45-minute video consultation with successful startup founders and venture advisors on pitch strategy & scaling.",
                        tokenCost = 2000, // 10x increased cost
                        category = "Mentorship",
                        emoji = "💡",
                        stockAvailable = 10,
                        impactClaim = "Expert guidance for aspiring entrepreneurs and business acceleration"
                    ),
                    RewardItem(
                        id = "rew_tree",
                        title = "Plant a Peepal Tree on Ganga Shore",
                        description = "Sponsor a tagged sacred Peepal/Banyan tree along Varanasi Ghats with your personalized name plaque.",
                        tokenCost = 600, // 10x increased cost
                        category = "Riverbank Flora",
                        emoji = "🌳",
                        stockAvailable = 25,
                        impactClaim = "Sequesters 48 lbs CO2 and stabilizes river embankments"
                    )
                )
                dao.insertRewards(updatedRewards)
            }

            suspend fun populateInitialData(dao: AppDao) {
                // Initial user with 100 tokens and 500 points bonus!
                val initialUser = UserProfile(
                    id = 1,
                    name = "Shivansh Pandey",
                    email = "pandey.shivansh2022@gmail.com",
                    tokens = 100, // 100 Welcome Bonus Tokens
                    points = 500, // 500 Welcome Bonus Points
                    totalCleanups = 4,
                    totalWasteCollectedKg = 42.8,
                    rankTitle = "Ganga Guardian • Tier II",
                    avatarEmoji = "🌊",
                    joinedDate = "August 2026",
                    isLoggedIn = false // Show clean sign in first
                )
                dao.insertUserProfile(initialUser)

                // Initial realistic Action Posts along River Ganga
                val samplePosts = listOf(
                    ActionPost(
                        id = 1,
                        title = "Assi Ghat to Tulsi Ghat Plastic Sweep",
                        description = "Organized a morning cleanup with 8 local volunteers. Collected non-biodegradable single-use plastic bottles, polythene wrappers, and nylon nets along the river bank slope.",
                        location = "Assi Ghat, Varanasi, Uttar Pradesh",
                        category = ActionCategory.PLASTIC_CLEANUP.name,
                        beforeImageUrl = "ganga_before_clean",
                        afterImageUrl = "ganga_after_clean",
                        tokensEarned = 35,
                        pointsEarned = 175,
                        wasteCollectedKg = 18.4,
                        areaCleanedSqM = 120.0,
                        timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 3, // 3 hours ago
                        authorName = "Shivansh Pandey",
                        authorAvatar = "🌊",
                        verificationStatus = VerificationStatus.AI_VERIFIED.name,
                        likesCount = 42,
                        isLikedByMe = true,
                        impactNote = "Prevented microplastic dispersion into active Dolphin nursery waters near Assi confluence."
                    ),
                    ActionPost(
                        id = 2,
                        title = "Har Ki Pauri Post-Aarti Flower & Debris Remediation",
                        description = "Cleared discarded floral offerings and clay diyas from stone steps, segregating organic waste for temple compost converters rather than letting it stagnate in the flow.",
                        location = "Har Ki Pauri, Haridwar, Uttarakhand",
                        category = ActionCategory.PUJA_WASTE_REMEDIATION.name,
                        beforeImageUrl = "ganga_before_clean",
                        afterImageUrl = "ganga_after_clean",
                        tokensEarned = 25,
                        pointsEarned = 120,
                        wasteCollectedKg = 14.2,
                        areaCleanedSqM = 85.0,
                        timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24, // 1 day ago
                        authorName = "Ananya Sharma",
                        authorAvatar = "🌸",
                        verificationStatus = VerificationStatus.COMMUNITY_CONFIRMED.name,
                        likesCount = 89,
                        isLikedByMe = false,
                        impactNote = "Diverted 14kg organic matter to local vermicompost units, keeping ghat water crystal clear."
                    ),
                    ActionPost(
                        id = 3,
                        title = "Triveni Ghat Sacred Riparian Bamboo & Vetiver Plantation",
                        description = "Planted soil-binding vetiver grass and indigenous bamboo saplings along the erosion-prone Ganga shoreline to stop siltation and absorb urban runoff naturally.",
                        location = "Triveni Ghat, Rishikesh, Uttarakhand",
                        category = ActionCategory.RIVERBANK_PLANTATION.name,
                        beforeImageUrl = "ganga_hero_banner",
                        afterImageUrl = "ganga_after_clean",
                        tokensEarned = 45,
                        pointsEarned = 220,
                        wasteCollectedKg = 10.0,
                        areaCleanedSqM = 200.0,
                        timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 48, // 2 days ago
                        authorName = "Rishi Kumar",
                        authorAvatar = "🌱",
                        verificationStatus = VerificationStatus.AI_VERIFIED.name,
                        likesCount = 134,
                        isLikedByMe = false,
                        impactNote = "Root network will filter up to 1,500L of surface water daily and prevent 5 tons of soil erosion yearly."
                    )
                )
                samplePosts.forEach { dao.insertActionPost(it) }

                // Initial Reward items in Marketplace (Value-driven: Scholarships, Coaching, Local Dining, Entrepreneurial Mentorship)
                syncUpdatedRewards(dao)
            }
        }
    }
}
