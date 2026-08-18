package com.example.cvmakerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cvmakerapp.ui.screens.SplashScreen
import com.example.cvmakerapp.ui.theme.CVMakerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CVMakerAppTheme {
                SplashScreen()
            }
        }
    }
}
