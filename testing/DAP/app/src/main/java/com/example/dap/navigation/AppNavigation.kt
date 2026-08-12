package com.example.dap.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.dap.ui.screens.splash.SplashScreen
import com.example.dap.ui.screens.onboarding.OnboardingScreen
import com.example.dap.ui.screens.auth.LoginScreen
import com.example.dap.ui.screens.auth.SignupScreen
import com.example.dap.ui.screens.auth.ForgotPasswordScreen
import com.example.dap.ui.screens.home.HomeScreen
import com.example.dap.ui.screens.doctors.DoctorListScreen
import com.example.dap.ui.screens.doctors.BookingScreen
import com.example.dap.ui.screens.appointments.AppointmentListScreen
import com.example.dap.ui.viewmodel.AuthViewModel
import com.example.dap.ui.viewmodel.AppointmentViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.dap.data.model.Doctor

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object DoctorList : Screen("doctor_list")
    object Booking : Screen("booking/{doctorId}") {
        fun createRoute(doctorId: Int) = "booking/$doctorId"
    }
    object Appointments : Screen("appointments")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    
    NavHost(
        navController = navController, 
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onSignIn = { email, password -> 
                    authViewModel.login(email, password) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onSignUp = {
                    navController.navigate(Screen.Signup.route)
                },
                onGoogleLogin = { /* Handle Google login */ },
                onFacebookLogin = { /* Handle Facebook login */ },
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.authError
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignUp = { name, email, password ->
                    authViewModel.signUp(name, email, password) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Signup.route) { inclusive = true }
                        }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.authError
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onResetPassword = { email ->
                    // Handle password reset logic (e.g., via ViewModel)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onSeeAllDoctors = {
                    navController.navigate(Screen.DoctorList.route)
                },
                onDoctorClick = { doctor ->
                    // Navigate to details or booking
                    navController.navigate(Screen.Booking.createRoute(doctor.id))
                },
                onBookClick = { doctor ->
                    navController.navigate(Screen.Booking.createRoute(doctor.id))
                },
                onBottomNavSelect = { index ->
                    when (index) {
                        1 -> navController.navigate(Screen.Appointments.route)
                        3 -> { // Profile / Logout
                             navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(Screen.DoctorList.route) {
            DoctorListScreen(
                onBackClick = { navController.popBackStack() },
                onDoctorClick = { doctor ->
                    navController.navigate(Screen.Booking.createRoute(doctor.id))
                },
                onBookClick = { doctor ->
                    navController.navigate(Screen.Booking.createRoute(doctor.id))
                }
            )
        }

        composable(
            route = Screen.Booking.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
            // In a real app, you'd fetch this from a repo. Here we just mock it for now.
            val doctor = listOf(
                Doctor(1, "Dr. Emily Carter", "Cardiologist", "City General Hospital", 4.8, 120, "$40", "dr_emily"),
                Doctor(2, "Dr. Michael Chen", "Dentist", "Smile Dental Clinic", 4.7, 85, "$30", "dr_michael"),
                Doctor(3, "Dr. Sarah Smith", "Pediatrician", "Children's Health Center", 4.9, 210, "$35", "dr_sarah"),
                Doctor(4, "Dr. James Wilson", "Neurologist", "Brain & Nerve Institute", 4.6, 92, "$50", "dr_james"),
                Doctor(5, "Dr. Sophia Brown", "Dermatologist", "Skin & Aesthetic Clinic", 4.8, 150, "$45", "dr_sophia"),
                Doctor(6, "Dr. Robert Miller", "Orthopedic", "Bone & Joint Hospital", 4.7, 110, "$55", "dr_robert")
            ).find { it.id == doctorId } ?: Doctor(1, "Dr. Emily Carter", "Cardiologist", "City General Hospital", 4.8, 120, "$40", "dr_emily")

            BookingScreen(
                doctor = doctor,
                onBackClick = { navController.popBackStack() },
                onConfirmBooking = { date, time ->
                    appointmentViewModel.bookAppointment(doctor, date, time)
                    navController.navigate(Screen.Appointments.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.Appointments.route) {
            val appointments by appointmentViewModel.appointments.collectAsState()
            AppointmentListScreen(
                appointments = appointments,
                onBackClick = { navController.popBackStack() },
                onCancelClick = { id -> appointmentViewModel.cancelAppointment(id) }
            )
        }
    }
}
