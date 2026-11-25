package com.simposio.practica17

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simposio.practica17.presentation.TasksScreen
import com.simposio.practica17.ui.theme.Practica17Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica17Theme {
                TasksScreen()
            }
        }
    }
}
