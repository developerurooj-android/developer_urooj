package com.example.dap.ui.screens.doctors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dap.data.model.Doctor
import com.example.dap.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    doctor: Doctor,
    onBackClick: () -> Unit,
    onConfirmBooking: (String, String) -> Unit
) {
    var selectedDateIndex by remember { mutableIntStateOf(0) }
    var selectedTime by remember { mutableStateOf("") }

    val times = listOf("09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM")
    
    val calendar = Calendar.getInstance()
    val availableDates = List(7) { index ->
        val date = calendar.clone() as Calendar
        date.add(Calendar.DAY_OF_YEAR, index)
        date
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Appointment", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundLight)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                color = White
            ) {
                Button(
                    onClick = { 
                        if (selectedTime.isNotEmpty()) {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            onConfirmBooking(dateFormat.format(availableDates[selectedDateIndex].time), selectedTime)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealBlue),
                    enabled = selectedTime.isNotEmpty()
                ) {
                    Text("Confirm Booking", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Doctor Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(LightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = TealBlue, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(doctor.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = DarkNavy)
                        Text(doctor.specialty, color = SoftGray, style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                            Text(" ${doctor.rating} (${doctor.reviewsCount} reviews)", style = MaterialTheme.typography.labelMedium, color = SoftGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Date Selection
            Text("Select Date", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = DarkNavy)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val numFormat = SimpleDateFormat("dd", Locale.getDefault())
                
                availableDates.take(4).forEachIndexed { index, date ->
                    val isSelected = selectedDateIndex == index
                    
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) TealBlue else White)
                            .clickable { selectedDateIndex = index }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            dayFormat.format(date.time),
                            color = if (isSelected) White else SoftGray,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            numFormat.format(date.time),
                            color = if (isSelected) White else DarkNavy,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Time Selection
            Text("Select Time", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = DarkNavy)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Fixed height for Grid to work inside vertical scroll
            Box(modifier = Modifier.height(220.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(times) { time ->
                        val isSelected = selectedTime == time
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) TealBlue else White)
                                .clickable { selectedTime = time }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                time,
                                color = if (isSelected) White else DarkNavy,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingPreview() {
    BookingScreen(
        doctor = Doctor(1, "Dr. Emily Carter", "Cardiologist", "City General Hospital", 4.8, 120, "$40", "dr_emily"),
        onBackClick = {},
        onConfirmBooking = { _, _ -> }
    )
}
