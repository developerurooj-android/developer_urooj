package com.example.dap.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dap.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit = {},
    onOptionClick: (String) -> Unit = {},
    onEditPhotoClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("Profile", fontWeight = FontWeight.Bold, color = DarkNavy) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Profile Header
            item {
                ProfileHeaderSection(
                    name = "Sarah Adams",
                    email = "sarah.adams@example.com",
                    onEditClick = onEditPhotoClick
                )
            }

            // 2. Personal Information
            item { SectionHeader("Personal Information") }
            item {
                ProfileCard {
                    ProfileInfoRow(icon = Icons.Default.Person, label = "Full Name", value = "Sarah Adams")
                    ProfileInfoRow(icon = Icons.Default.Wc, label = "Gender", value = "Female")
                    ProfileInfoRow(icon = Icons.Default.CalendarMonth, label = "Date of Birth", value = "12 May 1995")
                    ProfileInfoRow(icon = Icons.Default.Phone, label = "Phone", value = "+1 234 567 890", isLast = true)
                }
            }

            // 3. Medical Information
            item { SectionHeader("Medical Information") }
            item {
                ProfileCard {
                    ProfileInfoRow(icon = Icons.Default.Bloodtype, label = "Blood Group", value = "O+")
                    ProfileInfoRow(icon = Icons.Default.Warning, label = "Allergies", value = "Peanuts, Penicillin")
                    ProfileInfoRow(icon = Icons.Default.History, label = "Medical History", value = "Mild Asthma", isLast = true)
                }
            }

            // 4. Activity & Settings
            item { SectionHeader("General") }
            item {
                ProfileCard {
                    ProfileActionRow(icon = Icons.Default.EventNote, title = "My Appointments") { onOptionClick("appointments") }
                    ProfileActionRow(icon = Icons.Default.Favorite, title = "Favorite Doctors") { onOptionClick("favorites") }
                    ProfileActionRow(icon = Icons.Default.Payments, title = "Payment Methods") { onOptionClick("payments") }
                    ProfileActionRow(icon = Icons.Default.Notifications, title = "Notifications") { onOptionClick("notifications") }
                    ProfileActionRow(icon = Icons.Default.Settings, title = "Settings", isLast = true) { onOptionClick("settings") }
                }
            }

            // 5. Sign Out Button
            item {
                Spacer(modifier = Modifier.height(24.dp))
                LogoutButton(onLogoutClick)
            }
        }
    }
}

@Composable
fun ProfileHeaderSection(name: String, email: String, onEditClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            // Shadowed Avatar
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = LightBlue,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(55.dp))
                }
            }
            
            // Edit Floating Button
            SmallFloatingActionButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                containerColor = BrandBlue,
                contentColor = White
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit photo", modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DarkNavy
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LightBlue.copy(alpha = 0.5f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BrandBlue
        )
    }
}

@Composable
fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String, isLast: Boolean = false) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkNavy
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 0.5.dp,
                color = BackgroundLight
            )
        }
    }
}

@Composable
fun ProfileActionRow(icon: ImageVector, title: String, isLast: Boolean = false, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = DarkNavy,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(14.dp)
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 0.5.dp,
                color = BackgroundLight
            )
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Sign Out", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen()
}
