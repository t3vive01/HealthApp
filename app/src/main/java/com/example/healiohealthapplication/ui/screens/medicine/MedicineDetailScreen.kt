package com.example.healthapplication.ui.screens.medicine

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.components.TopNavBar
import com.example.healthapplication.ui.screens.shared.SharedViewModel
import com.example.healthapplication.ui.theme.pink

@Composable
fun MedicineDetailScreen(
    medicineId: String,
    medicineName: String,
    description: String,
    schedule: String,
    amount: String,
    duration: String,
    navController: NavController,
    viewModel: MedicineViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    // State to show confirmation dialog on delete
    val userData by sharedViewModel.userData.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val medicine by viewModel.medicine.collectAsState()
    val showDeleteDialog = remember { mutableStateOf(false) }

    LaunchedEffect(userData?.id) {
        userData?.id?.let { id ->
            viewModel.setUserId(id)
            viewModel.getMedicineById(id, medicineId)
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this medicine?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMedicine(medicineId) { success ->
                            if (success) {
                                navController.navigateUp() // Navigate back after successful deletion
                            }
                        }
                        showDeleteDialog.value = false
                    }
                ) {
                    Text("Yes", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = { TopNavBar(title = "Medicine Details", navController) },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            //contentAlignment = Alignment.TopCenter
        ) {

            medicine?.let { med ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(top = 70.dp, start = 12.dp, end = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Medicine Name
                    item {
                        Text(
                            text = med.name,
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // Medicine Icon with Circle Background
                   item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(120.dp)
                                .background(pink, shape = CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.medicine),
                                contentDescription = "Medicine Icon",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }

                    // Details of Medicine
                    item {
                        MedicineDescriptionCard(med.description)
                        MedicineDetailCard("Time & Schedule:", med.schedule)
                        MedicineDetailCard("Amount:", med.amount)
                        MedicineDetailCard("Duration:", med.duration)
                    }

                    item { Spacer(modifier = Modifier.height(32.dp)) }

                    // Edit Button
                    item {
                        Button(
                            onClick = {
                                navController.navigate("edit_medicine/${userId}/$medicineId") // Navigate to Edit screen
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = pink)
                        ) {
                            Text("Edit Medicine", color = Color.White, fontSize = 15.sp)
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    // Delete Button
                    item {
                        Button(
                            onClick = {
                                showDeleteDialog.value = true // Show confirmation dialog
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Delete Medicine", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}


// Detail Card for Medicine Data
@Composable
fun MedicineDetailCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$title ", fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = value, fontSize = 16.sp, color = Color.DarkGray)
        }
    }
}

// Medicine Description Card
@Composable
fun MedicineDescriptionCard(description: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = pink)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Description:", fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 16.sp, color = Color.White)
        }
    }
}
