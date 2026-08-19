package com.example.cvmakerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cvmakerapp.ui.theme.CVMakerAppTheme


// ------------------------------------------------------------
// DATA MODEL
// ------------------------------------------------------------

data class RecentCv(
    val title: String,
    val editedLabel: String,
    val tags: List<String>
)


// ------------------------------------------------------------
// HOME SCREEN
// ------------------------------------------------------------

@Composable
fun HomeScreen(
    userName: String = "User",
    cvs: List<RecentCv> = emptyList(),
    onCreateNewCv: () -> Unit = {},
    onOpenCv: (RecentCv) -> Unit = {},
    onCvMenuClick: (RecentCv) -> Unit = {}
) {

    Scaffold(               //provide foundation for UI
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            HomeTopBar()
        }

    ) { innerPadding ->

        HomeContent(
            modifier = Modifier.padding(innerPadding),
            userName = userName,
            cvs = cvs,
            onCreateNewCv = onCreateNewCv,
            onOpenCv = onOpenCv,
            onCvMenuClick = onCvMenuClick
        )
    }
}


// ------------------------------------------------------------
// TOP BAR
// ------------------------------------------------------------

@Composable
private fun HomeTopBar() {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // App logo + name

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = "CV Maker",
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(             //create empty space between ui elements
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "CV Maker",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }


        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}


// ------------------------------------------------------------
// HOME CONTENT
// ------------------------------------------------------------

@Composable             //takes data and call back so that parent decides ehat happens whe things are clicked
private fun HomeContent(
    modifier: Modifier = Modifier,
    userName: String,
    cvs: List<RecentCv>,
    onCreateNewCv: () -> Unit,
    onOpenCv: (RecentCv) -> Unit,
    onCvMenuClick: (RecentCv) -> Unit
) {

    LazyColumn(                 // don't load list at once

        modifier = modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 20.dp
        ),

        verticalArrangement = Arrangement.spacedBy(20.dp)

    ) {

        // ----------------------------------------------------
        // GREETING
        // ----------------------------------------------------

        item {

            Column {

                Text(
                    text = "Hello, $userName!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Ready to land your next role?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }


        // ----------------------------------------------------
        // CREATE NEW CV CARD
        // ----------------------------------------------------

        item {

            CreateNewCvCard(
                onClick = onCreateNewCv
            )
        }


        // ----------------------------------------------------
        // RECENT CVS HEADER
        // ----------------------------------------------------

        item {

            Row(

                modifier = Modifier.fillMaxWidth(),           //modifier add style and behavior

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Text(
                    text = "Recent CVs",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )


                Box(

                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(50)
                        )
                        .background(
                            MaterialTheme.colorScheme
                                .surfaceContainerHigh
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )

                ) {

                    Text(
                        text = "${cvs.size} Created",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }


        // ----------------------------------------------------
        // RECENT CV LIST
        // ----------------------------------------------------

        items(
            items = cvs,
            key = { cv -> cv.title }
        ) { cv ->

            RecentCvCard(

                cv = cv,

                onClick = {
                    onOpenCv(cv)
                },

                onMenuClick = {
                    onCvMenuClick(cv)
                }
            )
        }

    }
}


// ------------------------------------------------------------
// CREATE NEW CV CARD
// ------------------------------------------------------------

@Composable
private fun CreateNewCvCard(
    onClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(
                MaterialTheme.colorScheme.primary
            )
            .padding(24.dp),

        verticalAlignment =
            Alignment.CenterVertically,

        horizontalArrangement =
            Arrangement.SpaceBetween

    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Create New CV",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Create your professional CV using our fixed template.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.7f
                )
            )
        }


        Spacer(
            modifier = Modifier.width(12.dp)
        )


        Box(

            modifier = Modifier
                .size(56.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    MaterialTheme.colorScheme.onPrimary
                )
                .clickable(
                    onClick = onClick
                ),

            contentAlignment = Alignment.Center

        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Create New CV",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


// ------------------------------------------------------------
// RECENT CV CARD
// ------------------------------------------------------------

@Composable
private fun RecentCvCard(
    cv: RecentCv,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            )
            .clickable(
                onClick = onClick
            )
            .padding(20.dp)

    ) {

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween

        ) {

            Icon(
                imageVector = Icons.Outlined.InsertDriveFile,
                contentDescription = "CV",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )


            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable(
                    onClick = onMenuClick
                )
            )
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Text(
            text = cv.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )


        Spacer(
            modifier = Modifier.height(4.dp)
        )


        Text(
            text = cv.editedLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Row(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            cv.tags.forEach { tag ->

                TagChip(
                    text = tag
                )
            }
        }
    }
}


// ------------------------------------------------------------
// TAG CHIP
// ------------------------------------------------------------

@Composable
private fun TagChip(
    text: String
) {

    Box(

        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh
            )
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )

    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// ------------------------------------------------------------
// NEW RESUME CARD
// ------------------------------------------------------------

@Composable
private fun NewResumeDashedCard(
    onClick: () -> Unit
) {

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(MaterialTheme.shapes.large)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLow
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            )
            .clickable(
                onClick = onClick
            )

    ) {


        // Center content

        Column(

            modifier = Modifier.align(
                Alignment.Center
            ),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {

            Icon(
                imageVector =       //ImageVector = describes a vector graphic.
                    Icons.Outlined.AddCircleOutline,

                contentDescription =
                    "Create new resume",

                tint =
                    MaterialTheme.colorScheme.onSurfaceVariant,

                modifier =
                    Modifier.size(28.dp)
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Text(
                text = "New Resume",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        // Floating add button

        Box(

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(48.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primary
                )
                .clickable(
                    onClick = onClick
                ),

            contentAlignment = Alignment.Center

        ) {

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Resume",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


// ------------------------------------------------------------
// PREVIEW
// ------------------------------------------------------------

@Preview(               //show preview without running app in android studio
    showBackground = true,
    showSystemUi = true,
    heightDp = 900
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    heightDp = 900
)
@Composable             //preview your Compose UI inside Android Studio without running the whole app
private fun HomeScreenPreview() {
    CVMakerAppTheme {
        HomeScreen(
            userName = "Alex",
            cvs = listOf(
                RecentCv(
                    title = "Software Engineer CV",
                    editedLabel = "Edited 2 hours ago",
                    tags = listOf("TECH", "COMPLETE")
                ),
                RecentCv(
                    title = "Marketing Specialist Resume",
                    editedLabel = "Edited 2 days ago",
                    tags = listOf("MARKETING", "DRAFT")
                )
            )
        )
    }
}
