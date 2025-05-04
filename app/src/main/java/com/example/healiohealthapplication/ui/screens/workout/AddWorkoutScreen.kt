package com.example.healthapplication.ui.screens.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.healthapplication.R
import com.example.healthapplication.data.models.Workout
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.components.TopNavBar
import com.example.healthapplication.data.repository.WorkoutsRepository
import com.example.healthapplication.ui.theme.pink
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddWorkoutScreen(navController: NavController, userId: String) {
    // Local states for workout name and duration
    var workoutName by remember { mutableStateOf("") }
    var workoutDuration by remember { mutableStateOf("") }

    // Snackbar state for showing messages
    val snackbarHostState = remember { SnackbarHostState() }

    // Coroutine scope to launch save action
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopNavBar("Add New Workout", navController) },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Text("New Workout", fontSize = 30.sp, color = Color.Black) }


                // Input for workout name
                item { TextField(
                    value = workoutName,
                    onValueChange = { workoutName = it },
                    label = { Text("Workout Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF80CBC4).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF80CBC4),
                        unfocusedIndicatorColor = Color.Gray
                    )
                ) }


                // Input for workout duration
                item { TextField(
                    value = workoutDuration,
                    onValueChange = { workoutDuration = it },
                    label = { Text("Workout Duration (e.g., 30 min)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF80CBC4).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF80CBC4),
                        unfocusedIndicatorColor = Color.Gray
                    )
                ) }


                // Save button
                item { Spacer(modifier = Modifier.height(16.dp)) }

                item { Button(
                    onClick = {
                        if (userId.isNotEmpty()) {
                            val newWorkout = Workout(
                                name = workoutName,
                                duration = workoutDuration,
                                id = userId // Using userId passed from the navigation route
                            )

                            // Launch a coroutine to save workout
                            coroutineScope.launch {
                                try {
                                    // Use repository to save the workout
                                    val success =
                                        WorkoutsRepository(FirebaseFirestore.getInstance()).saveWorkout(
                                            userId,
                                            newWorkout
                                        )

                                    if (success) {
                                        // Call showSnackbar within a coroutine
                                        snackbarHostState.showSnackbar("Workout Saved Successfully!")
                                        navController.popBackStack() // Navigate back after success
                                    } else {
                                        snackbarHostState.showSnackbar("Failed to Save Workout!")
                                    }
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Error: ${e.localizedMessage}")
                                }
                            }
                        } else {
                            // Show snackbar error if userId is empty
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("User ID is missing!")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = pink)
                ) {
                    Text("Save Workout", color = Color.White, fontSize = 15.sp)
                } }

            }
        }
    }
}

