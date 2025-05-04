package com.example.healthapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.navigation.Routes
import com.example.healthapplication.ui.components.TopNavBarIconButton
import com.example.healthapplication.ui.theme.pink
import com.example.healthapplication.ui.theme.NavBarTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopNavBar(
    title: String, navController:
    NavController, expanded: Boolean,
    toggleExpanded: (Boolean) -> Unit,
    viewModel: HomeScreenViewModel
)  {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = pink,
            titleContentColor = MaterialTheme.colorScheme.secondary
        ),
        title = { Text(
            text = title,
            color = NavBarTextWhite
        ) },
        actions = {
            TopNavBarIconButton(
                contentDescription = R.string.home_screen_top_nav_bar_icon_description,
                icon = Icons.Filled.Menu,
                onClick = { toggleExpanded(!expanded) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { toggleExpanded(false) },
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
            ) {
                ItemForDropdownMenu(
                    textResId = R.string.home_screen_top_nav_bar_dropdown_text_for_workout,
                    onClick = { navController.navigate(Routes.WORKOUT) }
                )
                ItemForDropdownMenu(
                    textResId = R.string.home_screen_top_nav_bar_dropdown_text_for_medicine,
                    onClick = { navController.navigate(Routes.MEDICINE) }
                )
                ItemForDropdownMenu(
                    textResId = R.string.home_screen_top_nav_bar_dropdown_text_for_steps,
                    onClick = { navController.navigate(Routes.STEPS) }
                )
                ItemForDropdownMenu(
                    textResId = R.string.home_screen_top_nav_bar_dropdown_text_for_logout,
                    onClick = { viewModel.logout(navController) },
                    fontWeight = FontWeight.Bold
                )
            }
        }
)
}