package com.example.dap.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dap.ui.theme.*
import com.example.dap.data.model.Doctor

data class Category(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val backgroundColor: Color
)

val categories = listOf(
    Category(1, "Cardiology", Icons.Default.Favorite, Color(0xFFFFEBEE)),
    Category(2, "Dentist", Icons.Default.AddCircle, Color(0xFFE8F5E9)),
    Category(3, "Neurology", Icons.Default.Face, Color(0xFFE3F2FD)),
    Category(4, "Orthopedic", Icons.Default.Build, Color(0xFFFFF3E0)),
    Category(5, "Pediatrics", Icons.Default.Face, Color(0xFFF3E5F5)),
    Category(6, "Eye Care", Icons.Default.Info, Color(0xFFE0F2F1))
)

val topDoctors = listOf(
    Doctor(1, "Dr. Emily Carter", "Cardiologist", "City General Hospital", 4.8, 120, "$40", "dr_emily"),
    Doctor(2, "Dr. Michael Chen", "Dentist", "Smile Dental Clinic", 4.7, 85, "$30", "dr_michael"),
    Doctor(3, "Dr. Sarah Smith", "Pediatrician", "Children's Health Center", 4.9, 210, "$35", "dr_sarah"),
    Doctor(4, "Dr. James Wilson", "Neurologist", "Brain & Nerve Institute", 4.6, 92, "$50", "dr_james")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearch: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onCategoryClick: (Category) -> Unit = {},
    onDoctorClick: (Doctor) -> Unit = {},
    onBookClick: (Doctor) -> Unit = {},
    onSeeAllDoctors: () -> Unit = {},
    onSeeAllCategories: () -> Unit = {},
    onBottomNavSelect: (Int) -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = {
            HomeBottomNavigation(
                selectedIndex = selectedTab,
                onItemSelected = { 
                    selectedTab = it
                    onBottomNavSelect(it)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. Header
            item {
                HomeHeader(onNotificationClick)
            }

            // 2. Search Bar
            item {
                HomeSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = onSearch
                )
            }

            // 3. Upcoming Appointment
            item {
                UpcomingAppointmentCard()
            }

            // 4. Categories Header
            item {
                SectionHeader(
                    title = "Categories",
                    onSeeAllClick = onSeeAllCategories
                )
            }

            // 5. Categories Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        CategoryItem(category, onClick = { onCategoryClick(category) })
                    }
                }
            }

            // 6. Top Doctors Header
            item {
                SectionHeader(
                    title = "Top Doctors",
                    onSeeAllClick = onSeeAllDoctors
                )
            }

            // 7. Top Doctors List
            items(topDoctors) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onClick = { onDoctorClick(doctor) },
                    onBookClick = { onBookClick(doctor) }
                )
            }
        }
    }
}

@Composable
fun HomeHeader(onNotificationClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Avatar",
                tint = SoftGray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hello, Sarah 👋",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkNavy
            )
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.bodySmall,
                color = SoftGray
            )
        }

        // Notification
        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(White)
        ) {
            Box {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = DarkNavy
                )
                // Red badge
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
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
}

@Composable
fun UpcomingAppointmentCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = TealBlue)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Doctor Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Dr. Emily Carter",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Text(
                        text = "Cardiologist",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Date & Time
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(White.copy(alpha = 0.1f))
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mon, 24 June", style = MaterialTheme.typography.bodySmall, color = White)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("10:30 AM", style = MaterialTheme.typography.bodySmall, color = White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /* Reschedule */ },
                border = androidx.compose.foundation.BorderStroke(1.dp, White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White)
            ) {
                Text("Reschedule", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = DarkNavy
        )
        TextButton(onClick = onSeeAllClick) {
            Text("See All", color = TealBlue, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(category.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = TealBlue,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            color = DarkNavy,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onClick: () -> Unit, onBookClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = TealBlue)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkNavy
                )
                Text(
                    text = "${doctor.specialty} | ${doctor.hospital}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${doctor.rating}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${doctor.reviewsCount} reviews)",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftGray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = doctor.fee,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TealBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onBookClick,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealBlue)
                ) {
                    Text("Book", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HomeBottomNavigation(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("Home", Icons.Default.Home, 0),
            Triple("Appointments", Icons.Default.Event, 1),
            Triple("Messages", Icons.Default.Chat, 2),
            Triple("Profile", Icons.Default.Person, 3)
        )

        items.forEach { (label, icon, index) ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TealBlue,
                    selectedTextColor = TealBlue,
                    indicatorColor = TealBlue.copy(alpha = 0.1f),
                    unselectedIconColor = SoftGray,
                    unselectedTextColor = SoftGray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}
