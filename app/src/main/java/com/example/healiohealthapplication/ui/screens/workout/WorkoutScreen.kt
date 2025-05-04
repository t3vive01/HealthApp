package com.example.healthapplication.ui.screens.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthapplication.data.models.Workout
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.components.TopNavBar
import com.example.healthapplication.ui.screens.shared.SharedViewModel
import com.example.healthapplication.ui.theme.pink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WorkoutScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    workoutViewModel: WorkoutViewModel = hiltViewModel()
) {
    val userData by sharedViewModel.userData.collectAsState()
    val workouts by workoutViewModel.workouts.collectAsState()
    val isLoading by workoutViewModel.isLoading.collectAsState()
    val errorMessage by workoutViewModel.errorMessage.collectAsState()

    // Extract userId safely
    val userId = userData?.id

    // Ensure we fetch workouts only when userId is available
    LaunchedEffect(userId) {
        userId?.let { workoutViewModel.fetchWorkouts(it) }
    }

    Scaffold(
        topBar = { TopNavBar("Workouts", navController) },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Workout Tracker",
                    fontSize = 30.sp,
                    color = Color.White
                )


                // Show loading indicator
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                // Show error message if any
                errorMessage?.let {
                    Text(
                        it,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                // Display workouts only if userId is not null
                userId?.let { uid ->
                    workouts.forEach { workout ->
                        WorkoutCard(
                            workout = workout,
                            onEdit = { navController.navigate("edit_workout/$uid/${workout.id}/${workout.name}/${workout.duration}") },
                            onDelete = {
                                workoutViewModel.deleteWorkout(uid, workout.id) { success ->
                                    if (success) {
                                        // Handle successful deletion
                                    }
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { navController.navigate("add_workout/$uid") },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = pink)
                    ) {
                        Text(
                            "Add New Workout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }


                } ?: run {
                    Text(
                        "Loading user data...",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}




@Composable
fun WorkoutCard(workout: Workout, onEdit: () -> Unit, onDelete: () -> Unit) {
    var isTimerRunning by remember { mutableStateOf(false) } // Timer state
    var elapsedTime by remember { mutableStateOf(0) } // Elapsed time in seconds

    val coroutineScope = rememberCoroutineScope()

    // Start Timer logic
    val startTimer: () -> Unit = {
        isTimerRunning = true
        coroutineScope.launch {
            while (isTimerRunning) {
                delay(1000) // Wait for 1 second
                elapsedTime += 1 // Increment elapsed time
            }
        }
    }

    // Stop Timer logic
    val stopTimer: () -> Unit = {
        isTimerRunning = false // Stops the timer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(workout.name, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(workout.duration, fontSize = 17.sp, color = Color.Gray)
                }
                Row {
                    // Edit Button
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    // Delete Button
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timer functionality for workout progress tracking
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        if (isTimerRunning) stopTimer() else startTimer() // Toggle start/pause
                    }
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = if (isTimerRunning) "Pause Timer" else "Start Timer",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable {
                            if (isTimerRunning) stopTimer() else startTimer() // Toggle start/pause
                        }
                        .background(
                            color = pink,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Elapsed Time: ${elapsedTime / 60}:${elapsedTime % 60}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}










