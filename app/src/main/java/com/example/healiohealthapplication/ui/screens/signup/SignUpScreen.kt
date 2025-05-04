package com.example.healthapplication.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthapplication.R
import com.example.healthapplication.navigation.Routes
import com.example.healthapplication.ui.components.BigButton
import com.example.healthapplication.ui.components.ChangePageText
import com.example.healthapplication.ui.components.ErrorCard
import com.example.healthapplication.ui.components.LoginAndSignUpOutlinedTextField
import com.example.healthapplication.ui.screens.shared.SharedViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel, sharedViewModel: SharedViewModel) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(R.string.sign_up_screen_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            item { Spacer(modifier = Modifier.height(34.dp)) }
            item {
                LoginAndSignUpOutlinedTextField(
                    value = viewModel.currentEmail,
                    onValueChange = { viewModel.currentEmail = it },
                    label = stringResource(R.string.sign_up_screen_outlined_text_field_label_email),
                    keyboardType = KeyboardType.Email,
                    leadingIcon = Icons.Filled.Email,
                    iconContentDescription = stringResource(R.string.sign_up_screen_outlined_text_field_icon_desc_email),
                )
            }
            item {
                LoginAndSignUpOutlinedTextField(
                    value = viewModel.currentPassword,
                    onValueChange = { viewModel.currentPassword = it },
                    label = stringResource(R.string.sign_up_screen_outlined_text_field_label_password),
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    leadingIcon = Icons.Filled.Lock,
                    iconContentDescription = stringResource(R.string.sign_up_screen_outlined_text_field_icon_desc_password),
                )
            }
            item {
                Text(
                    text = stringResource(R.string.sign_up_screen_password_requirements_text),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )
            }
            item {
                viewModel.showError.takeIf { it }?.let {
                    ErrorCard(
                        errorMessage = viewModel.errorMessage ?: "Unknown error occurred.",
                        onDismiss = { viewModel.showError = false }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(34.dp)) }
            item {
                BigButton(
                    text = stringResource(R.string.sign_up_screen_submit_button_text),
                    onClick = {
                        viewModel.register(
                            navController,
                            viewModel.currentEmail,
                            viewModel.currentPassword
                        ) { userId ->
                            sharedViewModel.fetchUserData(userId)
                        }
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                ChangePageText(
                    text = stringResource(R.string.sign_up_screen_change_page_text),
                    linkText = stringResource(R.string.sign_up_screen_change_page_link_text)
                ) { navController.navigate(Routes.LOGIN) }
            }
        }
    }
}