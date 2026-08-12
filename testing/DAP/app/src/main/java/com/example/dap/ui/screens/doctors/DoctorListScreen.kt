package com.example.dap.ui.screens.doctors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dap.data.model.Doctor
import com.example.dap.ui.screens.home.DoctorCard
import com.example.dap.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    onBackClick: () -> Unit,
    onDoctorClick: (Doctor) -> Unit,
    onBookClick: (Doctor) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val allDoctors = listOf(
        Doctor(1, "Dr. Emily Carter", "Cardiologist", "City General Hospital", 4.8, 120, "$40", "dr_emily"),
        Doctor(2, "Dr. Michael Chen", "Dentist", "Smile Dental Clinic", 4.7, 85, "$30", "dr_michael"),
        Doctor(3, "Dr. Sarah Smith", "Pediatrician", "Children's Health Center", 4.9, 210, "$35", "dr_sarah"),
        Doctor(4, "Dr. James Wilson", "Neurologist", "Brain & Nerve Institute", 4.6, 92, "$50", "dr_james"),
        Doctor(5, "Dr. Sophia Brown", "Dermatologist", "Skin & Aesthetic Clinic", 4.8, 150, "$45", "dr_sophia"),
        Doctor(6, "Dr. Robert Miller", "Orthopedic", "Bone & Joint Hospital", 4.7, 110, "$55", "dr_robert")
    )

    val filteredDoctors = allDoctors.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.specialty.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doctors", fontWeight = FontWeight.Bold, color = DarkNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundLight)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                placeholder = { Text("Search doctor, specialty...", color = SoftGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SoftGray) },
                trailingIcon = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = TealBlue)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedBorderColor = TealBlue.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredDoctors) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        onClick = { onDoctorClick(doctor) },
                        onBookClick = { onBookClick(doctor) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorListPreview() {
    DoctorListScreen(onBackClick = {}, onDoctorClick = {}, onBookClick = {})
}
