package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.UserProfile
import com.example.ui.screens.ActionCelebrationDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ForgotPasswordDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RewardRedeemedDialog
import com.example.ui.screens.UploadActionDialog

@Composable
fun SanskowikApp(
    viewModel: SanskowikViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val actionPosts by viewModel.actionPosts.collectAsStateWithLifecycle()
    val rewards by viewModel.rewards.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val showUploadDialog by viewModel.showUploadDialog.collectAsStateWithLifecycle()
    val showForgotPasswordDialog by viewModel.showForgotPasswordDialog.collectAsStateWithLifecycle()
    val celebrationPost by viewModel.celebrationPost.collectAsStateWithLifecycle()
    val redeemedReward by viewModel.redeemedReward.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isLoggedIn) {
                AuthScreen(
                    onSignIn = { email, pass, onResult ->
                        viewModel.signIn(email, pass, onResult)
                    },
                    onRegister = { name, email, pass, onResult ->
                        viewModel.register(name, email, pass, onResult)
                    },
                    onForgotPasswordClick = {
                        viewModel.openForgotPasswordDialog()
                    }
                )
            } else {
                val currentUser = userProfile ?: UserProfile(
                    tokens = 100,
                    points = 500,
                    name = "Shivansh Pandey"
                )

                HomeScreen(
                    user = currentUser,
                    actionPosts = actionPosts,
                    rewards = rewards,
                    hotspots = viewModel.hotspots,
                    leaderboard = viewModel.getLeaderboard(),
                    selectedTab = selectedTab,
                    onTabSelected = { viewModel.setTab(it) },
                    onUploadClick = { viewModel.openUploadDialog() },
                    onLikeClick = { viewModel.toggleLike(it) },
                    onRedeemReward = { viewModel.redeemReward(it) },
                    onPlayGame = { title, fee, tokens, coins ->
                        viewModel.playMiniGame(title, fee, tokens, coins)
                    },
                    onLogout = { viewModel.logout() }
                )
            }

            // Dialogs
            if (showUploadDialog) {
                UploadActionDialog(
                    onDismiss = { viewModel.closeUploadDialog() },
                    onSubmit = { title, desc, loc, cat, bImg, aImg, vUri, wasteKg, areaSqM ->
                        viewModel.submitEnvironmentalAction(
                            title, desc, loc, cat, bImg, aImg, vUri, wasteKg, areaSqM
                        )
                    }
                )
            }

            if (showForgotPasswordDialog) {
                ForgotPasswordDialog(
                    initialEmail = userProfile?.email ?: "pandey.shivansh2022@gmail.com",
                    onDismiss = { viewModel.closeForgotPasswordDialog() },
                    onSubmit = { email, callback ->
                        viewModel.sendPasswordReset(email, callback)
                    }
                )
            }

            celebrationPost?.let { post ->
                ActionCelebrationDialog(
                    post = post,
                    onDismiss = { viewModel.dismissCelebration() }
                )
            }

            redeemedReward?.let { rew ->
                RewardRedeemedDialog(
                    reward = rew,
                    onDismiss = { viewModel.dismissRedemption() }
                )
            }
        }
    }
}
