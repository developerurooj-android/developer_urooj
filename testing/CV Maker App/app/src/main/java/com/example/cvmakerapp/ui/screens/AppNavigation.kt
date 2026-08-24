package com.example.cvmakerapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cvmakerapp.data.CvRepository
import com.example.cvmakerapp.data.CvTemplate

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
                        title = cv.jobTitle.ifBlank { cv.fullName }.ifBlank { "Untitled CV" },
                        editedLabel = "Created recently",
                        tags = cv.skills.take(2)
                    )
                },
                onCreateNewCv = {
                    // Reset selected template to default when creating new CV
                    CvRepository.selectedTemplate = CvTemplate.CLASSIC
                    navController.navigate("template_selection")
                },
                onOpenCv = { recentCv ->

                    val cvData = cvs.find {
                        it.jobTitle == recentCv.title || it.fullName == recentCv.title
                    }

                    if (cvData != null) {
                        // Load the saved template from the CV
                        CvRepository.selectedTemplate = cvData.template
                        CvRepository.previewCv = cvData
                        navController.navigate("view_cv")
                    }
                }
            )
        }


        // -----------------------------
        // 3. TEMPLATE SELECTION SCREEN
        // -----------------------------
        composable("template_selection") {

            TemplateSelectionScreen(
                onTemplateSelected = { selectedTemplate ->
                    CvRepository.selectedTemplate = selectedTemplate
                    navController.navigate("create_cv")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // -----------------------------
        // 4. CREATE CV SCREEN
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
        // 5. VIEW CV SCREEN
        // -----------------------------
        composable("view_cv") {
            val cvData = CvRepository.previewCv
            if (cvData != null) {
                CvPreviewScreen(
                    cvData = cvData,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {
                        // Save CV to repository
                        CvRepository.saveCv(cvData)
                        navController.navigate("home") {
                            popUpTo("view_cv") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}
