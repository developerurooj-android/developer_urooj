package com.example.cvmakerapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cvmakerapp.data.CvRepository

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val cvs by CvRepository.cvs.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // -----------------------------
        // 1. SPLASH SCREEN
        // -----------------------------
        composable("splash") {

            SplashScreen(
                onSplashFinished = {

                    navController.navigate("home") {

                        // Remove splash from back stack
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // -----------------------------
        // 2. HOME SCREEN
        // -----------------------------
        composable("home") {

            HomeScreen(
                userName = "User",
                cvs = cvs.map { cv ->
                    RecentCv(
                        title = cv.jobTitle.ifBlank { cv.name }.ifBlank { "Untitled CV" },
                        editedLabel = "Created recently",
                        tags = cv.skills.take(2)
                    )
                },
                onCreateNewCv = {
                    navController.navigate("create_cv")
                },
                onOpenCv = { recentCv ->
                    // Find the original CV data by title (simple matching for now)
                    val cvData = cvs.find { it.jobTitle == recentCv.title || it.name == recentCv.title }
                    if (cvData != null) {
                        CvRepository.previewCv = cvData
                        navController.navigate("view_cv")
                    }
                }
            )
        }


        // -----------------------------
        // 3. CREATE CV SCREEN
        // -----------------------------
        composable("create_cv") {

            CreateCvScreen(

                // Back arrow → Home
                onBack = {
                    navController.popBackStack()
                },

                // Save → Home
                onSave = {
                    navController.popBackStack()
                },

                // Preview → View CV
                onPreview = {
                    navController.navigate("view_cv")
                }
            )
        }


        // -----------------------------
        // 4. VIEW CV SCREEN
        // -----------------------------
        composable("view_cv") {
            val cvData = CvRepository.previewCv
            if (cvData != null) {
                CvPreviewScreen(
                    cvData = cvData,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
