package com.example.healthapplication.ui.screens.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.healthapplication.data.repository.AuthRepository
import com.example.healthapplication.navigation.Routes
import com.example.healthapplication.utils.StepCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val stepCounter: StepCounter
) : ViewModel() {
    // -- states for current ui screen --
    var currentEmail by mutableStateOf("")
    var currentPassword by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var showError by mutableStateOf(false)

    fun login(navController: NavController, email: String, password: String, onLoginSuccess: (String) -> Unit) {
        authRepository.login(email, password) { success, error ->
            if (success) {
                authRepository.getCurrentUserId()?.let { userId ->
                    onLoginSuccess(userId)
                    navController.navigate(Routes.WELCOME)
                    stepCounter.startListening()
                }
            } else {
                errorMessage = error ?: "Registration failed."
                showError = true
            }
        }
    }
}