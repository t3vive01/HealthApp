package com.example.healthapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.ui.theme.pink
import com.example.healthapplication.ui.theme.NavBarTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    title: String,
    navController: NavController,
    onBackClick: () -> Unit = { navController.navigateUp() }
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = pink,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(
            text = title,
            color = NavBarTextWhite
        ) },
        navigationIcon = {
            TopNavBarIconButton(
                contentDescription = R.string.top_nav_bar_icon_description,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBackClick
            )
        },
    )
}
