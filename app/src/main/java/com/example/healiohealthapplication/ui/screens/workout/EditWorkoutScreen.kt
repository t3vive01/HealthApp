package com.example.healthapplication.ui.screens.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.data.models.Workout
import com.example.healthapplication.ui.components.TopNavBar
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.theme.pink

@Composable
fun EditWorkoutScreen(
    userId: String,
    workoutId: String,  // Changed from workoutName to workoutId
    workoutName: String,
    workoutDuration: String,
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel() // Inject ViewModel instead of repository
) {
    var updatedWorkoutName by remember { mutableStateOf(workoutName) }
    var updatedDuration by remember { mutableStateOf(workoutDuration) }
    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopNavBar(title = "Edit Workout", navController = navController) },
        bottomBar = { BottomNavBar(navController) }
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
                item { Text("Edit Workout", fontSize = 24.sp, fontWeight = FontWeight.Bold) }


                // TextField for workout name
                item { OutlinedTextField(
                    value = updatedWorkoutName,
                    onValueChange = { updatedWorkoutName = it },
                    label = { Text("Workout Name") },
                    modifier = Modifier.fillMaxWidth()
                ) }

                // TextField for workout duration
                item { OutlinedTextField(
                    value = updatedDuration,
                    onValueChange = { updatedDuration = it },
                    label = { Text("Workout Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                ) }

                // Save Button
                item { Button(
                    onClick = {
                        isSaving = true
                        val updatedWorkout = Workout(updatedWorkoutName, updatedDuration)
                        viewModel.updateWorkout(userId, workoutId, updatedWorkout) { success ->
                            isSaving = false
                            if (success) {
                                navController.popBackStack() // Go back to the previous screen on success
                            } else {
                                // Handle failure (could be a Toast or an error message)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = pink),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Save Changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } }


                // Cancel Button
                item { Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(
                        "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } }

            }
        }
    }
}
