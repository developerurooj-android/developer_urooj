package com.example.dap.ui.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dap.data.local.entity.Appointment
import com.example.dap.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentListScreen(
    appointments: List<Appointment>,
    onBackClick: () -> Unit,
    onCancelClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Appointments", fontWeight = FontWeight.Bold, color = DarkNavy) },
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
        if (appointments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No appointments yet", color = SoftGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(appointments) { appointment ->
                    AppointmentCard(appointment, onCancelClick)
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, onCancelClick: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TealBlue)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(appointment.doctorName, fontWeight = FontWeight.Bold, color = DarkNavy)
                    Text(appointment.doctorSpecialty, color = SoftGray, style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = SoftGray)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = SoftGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(appointment.date, style = MaterialTheme.typography.bodySmall, color = DarkNavy)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = SoftGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(appointment.time, style = MaterialTheme.typography.bodySmall, color = DarkNavy)
                }
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (appointment.status == "Upcoming") TealBlue.copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        appointment.status,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (appointment.status == "Upcoming") TealBlue else SoftGray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            if (appointment.status == "Upcoming") {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { onCancelClick(appointment.id) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { /* Reschedule */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealBlue)
                    ) {
                        Text("Reschedule")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppointmentListPreview() {
    AppointmentListScreen(
        appointments = listOf(
            Appointment(1, 1, 1, "Dr. Emily Carter", "Cardiologist", "2024-06-24", "10:30 AM", "Upcoming"),
            Appointment(2, 1, 2, "Dr. Michael Chen", "Dentist", "2024-06-20", "02:00 PM", "Completed")
        ),
        onBackClick = {},
        onCancelClick = {}
    )
}
