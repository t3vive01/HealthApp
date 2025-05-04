package com.example.healthapplication.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.navigation.Routes
import com.example.ohealthapplication.ui.components.BigButton
import com.example.healthapplication.ui.theme.BackgroundColour


@Composable
fun WelcomeScreen(
  navController: NavController,
  modifier: Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Main content column
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            item {
                Text(
                    text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.headlineMedium,
                    color = BackgroundColour,
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            item {
                BigButton(
                    text = stringResource(id = R.string.next),
                    onClick = { navController.navigate(Routes.HOME) }
                )

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
