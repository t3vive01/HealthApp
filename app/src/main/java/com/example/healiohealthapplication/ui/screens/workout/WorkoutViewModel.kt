package com.example.healthapplication.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthapplication.data.models.Workout
import com.example.healthapplication.data.repository.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchWorkouts(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = workoutsRepository.getWorkouts(userId)
                _workouts.value = result ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load workouts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteWorkout(userId: String, workoutId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = workoutsRepository.deleteWorkout(userId, workoutId)
                onResult(success)
                if (success) {
                    _workouts.value = _workouts.value.filterNot { it.id == workoutId }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete workout: ${e.message}"
                onResult(false)
            }
        }
    }

    fun updateWorkout(userId: String, workoutId: String, updatedWorkout: Workout, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = workoutsRepository.updateWorkout(userId, workoutId, updatedWorkout)
                onResult(success)
                if (success) fetchWorkouts(userId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update workout: ${e.message}"
                onResult(false)
            }
        }
    }
}
