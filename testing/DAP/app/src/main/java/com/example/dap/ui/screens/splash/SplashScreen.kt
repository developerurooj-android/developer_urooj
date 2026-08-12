package com.example.dap.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dap.ui.theme.BrandBlue
import com.example.dap.ui.theme.DeepBlue
import com.example.dap.ui.theme.LightBlue
import com.example.dap.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    // Animation States
    var startAnimation by remember { mutableStateOf(false) }
    
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "fade"
    )
    
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000) // 3 seconds total splash time
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Logo Container
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnim.value)
                    .alpha(alphaAnim.value),
                shape = CircleShape,
                color = White.copy(alpha = 0.9f),
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    MedicalLogo(modifier = Modifier.size(60.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. App Name
            Text(
                text = "DocCare",
                style = MaterialTheme.typography.displayLarge,
                color = DeepBlue,
                modifier = Modifier
                    .alpha(alphaAnim.value)
                    .offset(y = (20 * (1 - alphaAnim.value)).dp)
            )

            // 3. Tagline
            Text(
                text = "Your Health, Our Priority",
                style = MaterialTheme.typography.labelMedium,
                color = DeepBlue.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .alpha(alphaAnim.value)
            )
        }

        // 4. Premium Loading Dots at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            LoadingDots()
        }
    }
}

@Composable
fun MedicalLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Draw Shield Shape
        val path = Path().apply {
            moveTo(width * 0.5f, 0f)
            cubicTo(width * 0.8f, 0f, width, height * 0.2f, width, height * 0.5f)
            lineTo(width * 0.5f, height)
            lineTo(0f, height * 0.5f)
            cubicTo(0f, height * 0.2f, width * 0.2f, 0f, width * 0.5f, 0f)
            close()
        }
        drawPath(path, color = BrandBlue, style = Fill)

        // Draw Cross Cutout
        val crossWidth = width * 0.2f
        val crossHeight = height * 0.5f
        
        // Vertical Bar
        drawRect(
            color = White,
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.4f, height * 0.25f),
            size = androidx.compose.ui.geometry.Size(crossWidth, crossHeight)
        )
        // Horizontal Bar
        drawRect(
            color = White,
            topLeft = androidx.compose.ui.geometry.Offset(width * 0.25f, height * 0.4f),
            size = androidx.compose.ui.geometry.Size(width * 0.5f, crossWidth)
        )
    }
}

@Composable
fun LoadingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    
    @Composable
    fun Dot(delay: Int) {
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = delay, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dotScale"
        )
        
        Surface(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(8.dp)
                .scale(scale),
            shape = CircleShape,
            color = BrandBlue.copy(alpha = scale)
        ) {}
    }

    Row {
        Dot(0)
        Dot(200)
        Dot(400)
    }
}
