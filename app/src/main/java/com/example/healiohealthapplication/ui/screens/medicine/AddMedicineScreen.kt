package com.example.healthapplication.ui.screens.medicine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.data.models.Medicine
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.components.TopNavBar
import com.example.healthapplication.ui.theme.pink

@Composable
fun AddMedicineScreen(
    navController: NavController,
    viewModel: MedicineViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    // Set the userId when screen loads
    LaunchedEffect(Unit) {
        val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (!currentUserId.isNullOrEmpty()) {
            viewModel.setUserId(currentUserId)
        }
    }

    Scaffold(
        topBar = { TopNavBar("Add Medicine", navController) },
        bottomBar = { BottomNavBar(navController) },
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
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Text("New Medicine", fontSize = 20.sp, color = Color.Black) }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                item { Box(
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

                    Spacer(modifier = Modifier.height(16.dp)) }

                item { MedicineInputField("Medicine Name", name) { name = it } }
                item { MedicineInputField("Description", description) { description = it } }
                item { MedicineInputField("Time & Schedule", schedule) { schedule = it } }
                item { MedicineInputField("Amount", amount) { amount = it } }
                item { MedicineInputField("Duration", duration) { duration = it } }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {  Button(
                    onClick = {
                        try {
                            val medicine = Medicine(
                                name = name,
                                description = description,
                                schedule = schedule,
                                amount = amount,
                                duration = duration
                            )
                            viewModel.addMedicine(medicine) { success ->
                                if (success) {
                                    navController.navigateUp() // Navigate back after adding
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = pink)
                ) {
                    Text("Add Medicine", color = Color.White, fontSize = 15.sp)
                } }

            }
        }
    }
}

@Composable
fun MedicineInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Black) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textStyle = LocalTextStyle.current.copy(color = Color.Black)
    )
}
