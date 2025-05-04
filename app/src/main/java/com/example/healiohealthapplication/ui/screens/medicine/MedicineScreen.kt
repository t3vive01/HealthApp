package com.example.healthapplication.ui.screens.medicine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.healthapplication.navigation.Routes
import com.example.healthapplication.ui.components.BottomNavBar
import com.example.healthapplication.ui.components.TopNavBar
import com.example.healthapplication.ui.theme.pink
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MedicineScreen(navController: NavController, viewModel: MedicineViewModel = hiltViewModel()) {
    val medicines by viewModel.medicines.collectAsState()

    LaunchedEffect(Unit) {
        val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (!currentUserId.isNullOrEmpty()) {
            viewModel.setUserId(currentUserId)
            viewModel.fetchMedicines()
        }
    }

    Scaffold(
        topBar = { TopNavBar("Medicine", navController) },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_MEDICINE) },
                containerColor = pink
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medicine", tint = Color.White)
            }
        }
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
                item { Text("Medicine Reminder!", fontSize = 20.sp, color = Color.Black) }

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
                } }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item { if (medicines.isEmpty()) {
                    Text("No medicines added yet.", color = Color.White)
                } else {
                    MedicineListContent(medicines, navController)
                } }
            }
        }
    }
}

@Composable
fun MedicineListContent(medicines: List<Medicine>, navController: NavController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState())
    ) {
        medicines.forEach { medicine ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        try {
                            val id = medicine.id?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: "0"
                            val name = medicine.name?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: "Unknown"
                            val description = medicine.description?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: "No description"
                            val schedule = medicine.schedule?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: "No schedule"
                            val amount = medicine.amount?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: "No amount"
                            val duration = medicine.duration?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: "No duration"

                            navController.navigate("medicine_detail/$id/$name/$description/$schedule/$amount/$duration")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medicine),
                        contentDescription = "Pill",
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = medicine.name ?: "Unknown Medicine", fontSize = 16.sp)
                }
            }
        }
    }
}



