package com.example.healthapplication.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.healthapplication.data.models.Steps
import com.example.healthapplication.data.repository.AuthRepository
import com.example.healthapplication.data.repository.StepsRepository
import com.example.healthapplication.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val stepsRepository: StepsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // --- appbar states ---
    var expanded by mutableStateOf(false)
        private set

    // --- steps state ---
    private val _stepCount = MutableStateFlow<Steps?>(null)
    val stepCount: StateFlow<Steps?> = _stepCount

    fun toggleExpanded() {
        expanded = !expanded
    }

    fun loadSteps(userId: String) {
        viewModelScope.launch {
            stepsRepository.getStepData(userId) { data ->
                _stepCount.value = data
            }
        }
    }

    fun logout(navController: NavController) {
        authRepository.logout()
        navController.navigate(Routes.START)
    }
}
