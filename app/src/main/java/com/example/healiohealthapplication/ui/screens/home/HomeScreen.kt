package com.example.healthapplication.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthapplication.navigation.Routes
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.screens.shared.SharedViewModel
import com.example.healthapplication.ui.theme.pink

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel, sharedViewModel: SharedViewModel) {
    Scaffold(
        topBar = { HomeScreenTopNavBar("Home", navController, viewModel.expanded, { viewModel.toggleExpanded() }, viewModel = viewModel) },
        bottomBar = { BottomNavBar(navController) }

    ) { innerPadding ->
        // CollectAsState automatically observes changes.
        val userData by sharedViewModel.userData.collectAsState()
        val steps by viewModel.stepCount.collectAsState()

        LaunchedEffect(userData?.id) {
           userData?.id?.let { viewModel.loadSteps(it) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Spacer(modifier = Modifier.height(120.dp))

                Text(
                    text = "Today's steps",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Spacer(modifier = Modifier.width(36.dp))

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(
                                width = 2.dp,
                                color = pink,
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (steps?.dailyStepsTaken ?: 0).toString(),
                            style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))
            }

            item {
                Text(
                    text = "Today's activities",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(36.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navController.navigate(Routes.WORKOUT) }
                    ) {
                        Text(text = "Workout")
                    }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navController.navigate(Routes.MEDICINE) },
                        modifier = Modifier
                    ) {
                        Text(text = "Medicine")
                    }

                }
            }
        }
    }
}


