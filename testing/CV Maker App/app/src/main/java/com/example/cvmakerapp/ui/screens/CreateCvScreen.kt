package com.example.cvmakerapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.cvmakerapp.data.CvData
import com.example.cvmakerapp.data.CvRepository
import com.example.cvmakerapp.data.EducationEntry
import com.example.cvmakerapp.data.ExperienceEntry

@Composable
fun CreateCvScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onPreview: () -> Unit = {}
) {

    // ---------------------------------------------------------
    // CURRENT STEP
    // ---------------------------------------------------------

    var currentStep by remember {
        mutableIntStateOf(1)
    }

    // ---------------------------------------------------------
    // STEP 1 - PERSONAL INFORMATION
    // ---------------------------------------------------------

    var firstName by remember {
        mutableStateOf("")
    }

    var lastName by remember {
        mutableStateOf("")
    }

    // ---------------------------------------------------------
    // STEP 2 - PROFESSIONAL DETAILS
    // ---------------------------------------------------------

    var jobTitle by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    // ---------------------------------------------------------
    // STEP 3 - PROFILE / LINKS
    // ---------------------------------------------------------

    var profileImageUri by remember {
        mutableStateOf<String?>(null)
    }

    var linkedIn by remember {
        mutableStateOf("")
    }

    var website by remember {
        mutableStateOf("")
    }

    // ---------------------------------------------------------
    // STEP 4 - SUMMARY
    // ---------------------------------------------------------

    var summary by remember {
        mutableStateOf("")
    }

    // ---------------------------------------------------------
    // STEP 5 - EXPERIENCE
    // ---------------------------------------------------------

    var experiences by remember {
        mutableStateOf<List<ExperienceEntry>>(
            listOf(ExperienceEntry())
        )
    }

    // ---------------------------------------------------------
    // STEP 6 - EDUCATION
    // ---------------------------------------------------------

    var education by remember {
        mutableStateOf<List<EducationEntry>>(
            listOf(EducationEntry())
        )
    }

    // ---------------------------------------------------------
    // STEP 7 - SKILLS
    // ---------------------------------------------------------

    var skills by remember {
        mutableStateOf(
            emptyList<String>()
        )
    }

    // ---------------------------------------------------------
    // CREATE COMPLETE CV DATA
    // ---------------------------------------------------------

    val cvData = CvData(
        firstName = firstName,
        lastName = lastName,
        jobTitle = jobTitle,
        email = email,
        phone = phone,
        location = location,

        profileImageUri = profileImageUri,

        linkedIn = linkedIn,
        website = website,

        summary = summary,

        experiences = experiences.filter {
            it.company.isNotBlank() ||
                    it.role.isNotBlank()
        },

        education = education.filter {
            it.school.isNotBlank() ||
                    it.degree.isNotBlank()
        },

        skills = skills,
        
        template = CvRepository.selectedTemplate
    )

    // ---------------------------------------------------------
    // SAVE CV HANDLER
    // ---------------------------------------------------------

    val handleSave = {
        CvRepository.saveCv(cvData)
        onSave()
    }

    // ---------------------------------------------------------
    // STEP NAVIGATION
    // ---------------------------------------------------------

    when (currentStep) {

        // =====================================================
        // STEP 1
        // =====================================================

        1 -> {

            PersonalInformationStep(

                firstName = firstName,

                lastName = lastName,

                onFirstNameChange = {
                    firstName = it
                },

                onLastNameChange = {
                    lastName = it
                },

                onBack = onBack,

                onNext = {
                    currentStep = 2
                },

                onSave = handleSave
            )
        }

        // =====================================================
        // STEP 2
        // =====================================================

        2 -> {

            ProfessionalDetailsStep(

                jobTitle = jobTitle,

                email = email,

                phone = phone,

                location = location,

                onJobTitleChange = {
                    jobTitle = it
                },

                onEmailChange = {
                    email = it
                },

                onPhoneChange = {
                    phone = it
                },

                onLocationChange = {
                    location = it
                },

                onBack = {
                    currentStep = 1
                },

                onNext = {
                    currentStep = 3
                },

                onSave = handleSave
            )
        }

        // =====================================================
        // STEP 3
        // =====================================================

        3 -> {

            ProfileLinksStep(

                profileImageUri = profileImageUri,

                linkedIn = linkedIn,

                website = website,

                onImageSelected = {
                    profileImageUri = it
                },

                onLinkedInChange = {
                    linkedIn = it
                },

                onWebsiteChange = {
                    website = it
                },

                onBack = {
                    currentStep = 2
                },

                onNext = {
                    currentStep = 4
                },

                onSave = handleSave
            )
        }

        // =====================================================
        // STEP 4
        // =====================================================

        4 -> {

            ProfessionalSummaryStep(

                summary = summary,

                onSummaryChange = {
                    summary = it
                },

                onBack = {
                    currentStep = 3
                },

                onNext = {
                    currentStep = 5
                }
            )
        }

        // =====================================================
        // STEP 5
        // =====================================================

        5 -> {

            ExperienceStep(

                experiences = experiences,

                onExperiencesChange = {
                    experiences = it
                },

                onBack = {
                    currentStep = 4
                },

                onNext = {
                    currentStep = 6
                }
            )
        }

        // =====================================================
        // STEP 6
        // =====================================================

        6 -> {

            EducationStep(

                education = education,

                onEducationChange = {
                    education = it
                },

                onBack = {
                    currentStep = 5
                },

                onNext = {
                    currentStep = 7
                }
            )
        }

        // =====================================================
        // STEP 7
        // =====================================================

        7 -> {

            SkillsStep(

                skills = skills,

                onSkillsChange = {
                    skills = it
                },

                onBack = {
                    currentStep = 6
                },

                onPreview = {

                    // Save complete CV temporarily
                    CvRepository.previewCv = cvData

                    // Open preview screen
                    onPreview()
                }
            )
        }
    }
}