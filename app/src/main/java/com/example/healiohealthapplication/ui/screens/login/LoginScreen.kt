package com.example.healthapplication.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun LoginScreen(navController: NavController, viewModel: LoginViewModel, sharedViewModel: SharedViewModel) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(R.string.login_screen_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            item { Spacer(modifier = Modifier.height(34.dp)) }
            item {
                LoginAndSignUpOutlinedTextField(
                    value = viewModel.currentEmail,
                    onValueChange = { viewModel.currentEmail = it },
                    label = stringResource(R.string.login_screen_outlined_text_field_label_email),
                    keyboardType = KeyboardType.Email,
                    leadingIcon = Icons.Filled.Email,
                    iconContentDescription = stringResource(R.string.login_screen_outlined_text_field_icon_description_email)
                )
            }
            item {
                LoginAndSignUpOutlinedTextField(
                    value = viewModel.currentPassword,
                    onValueChange = { viewModel.currentPassword = it },
                    label = stringResource(R.string.login_screen_outlined_text_field_label_password),
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    leadingIcon = Icons.Filled.Lock,
                    iconContentDescription = stringResource(R.string.login_screen_outlined_text_field_icon_description_password)
                )
            }
            item {
                viewModel.showError.takeIf { it }?.let {
                    ErrorCard(
                        errorMessage = viewModel.errorMessage ?: stringResource(R.string.login_screen_error_card_in_case_of_null_error_message),
                        onDismiss = { viewModel.showError = false }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(34.dp)) }
            item {
                BigButton(
                    text = stringResource(R.string.login_screen_submit_button_text),
                    onClick = {
                        viewModel.login(
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
                    text = stringResource(R.string.login_screen_change_page_text_normal),
                    linkText = stringResource(R.string.login_screen_change_page_text_link)
                ) { navController.navigate(Routes.SIGNUP) }
            }
        }
    }
}