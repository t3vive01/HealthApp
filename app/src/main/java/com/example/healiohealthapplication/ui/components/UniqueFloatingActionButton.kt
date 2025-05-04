package com.example.healthapplication.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.healthapplication.navigation.Routes

@Composable
fun UniqueFloatingActionButton(
    navController: NavController,
    route: String,
    icon: ImageVector,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = { navController.navigate(route) },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}