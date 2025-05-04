package com.example.healthapplication.ui.screens.steps

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ohealthapplication.data.models.Steps
import com.example.healthapplication.data.repository.StepsRepository
import com.example.healthapplication.utils.StepCounter
import com.example.healthapplication.utils.StepPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val firestoreRepository: StepsRepository,
    private val stepPrefs: StepPrefs,
    private val stepCounter: StepCounter,
    private val permissions: Permissions
) : ViewModel() {
    val isStepTrackingSupported = stepCounter.isStepTrackingSupported
    val currentlyUsedSensor = stepCounter.currentlyUsedSensor
    var currentStepGoal by mutableStateOf<Int?>(0)
    private var lastStepCount = 0
    var userId: String? = null

    // -- UI values --
    private val _stepsData = MutableStateFlow<Steps?>(null)
    val stepsData: StateFlow<Steps?> = _stepsData

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission

    var showNotSupportedError by mutableStateOf(true)
    var showNoPermissionError by mutableStateOf(true)

    // gets steps from the stepCounter and updates firestore using updateStepsTakenData
    private fun collectSteps() {
        userId?.let { id ->
            viewModelScope.launch { // cancels when viewmodel is cleared
                stepCounter.stepCount.collect { newTotalSteps ->
                    val today = getTodayDate()
                    val savedDate = stepPrefs.getLastResetDate()
                    if (savedDate == null) { stepPrefs.setLastResetDate(today) }
                    if (savedDate != today) {
                        stepPrefs.setLastResetDate(today)
                        resetStepsForNewDay(userId = id)
                    }
                    val originalSteps = stepPrefs.getLastStepCount()
                    val stepsDifference = newTotalSteps + originalSteps
                    if (stepsDifference > 0) {
                        updateStepsTakenData(userId = id, stepsDifference)
                    }
                }
            }
        }
    }

    // calculates how many steps walked / the goal
    private fun updateStepsProgress() {
        val stepsTaken = stepsData.value?.dailyStepsTaken ?: 0
        val stepGoal = stepsData.value?.dailyStepGoal ?: 10000
        _progress.value = if (stepGoal > 0) (stepsTaken.toFloat() / stepGoal).coerceIn(0f, 1f) else 0f
    }

    // get current step goal and steps if they exist
    fun getCurrentStepData(userId: String) {
        firestoreRepository.getStepData(userId) { steps ->
            if (steps != null) {
                _stepsData.value = Steps(dailyStepsTaken = steps.dailyStepsTaken, dailyStepGoal = steps.dailyStepGoal)
                currentStepGoal = steps.dailyStepGoal
                updateStepsProgress()
                val startUp = stepPrefs.getStartUpBoolean()
                if (startUp) {
                    lastStepCount = steps.dailyStepsTaken
                    stepPrefs.setLastStepCount(lastStepCount)
                    stepPrefs.setStartUpBoolean(false)
                }
                val hasPermissionNow = hasPermission.value
                Log.e("StepViewModel", "hasPermission in getCurrentStepData value: $hasPermissionNow")
                if (hasPermissionNow) {
                    collectSteps()
                }
            } else {
                initializeStepsData(userId)
            }
        }
    }

    // adds the empty daily steps and a set step goal into firestore
    private fun initializeStepsData(userId: String, dailyStepGoal: Int = 10000) {
        firestoreRepository.createStepsData(userId, dailyStepGoal) { success ->
            if (success) {
                _stepsData.value = Steps(dailyStepsTaken = 0, dailyStepGoal = dailyStepGoal)
                currentStepGoal = dailyStepGoal
                val startUp = stepPrefs.getStartUpBoolean()
                if (startUp) {
                    lastStepCount = 0
                    stepPrefs.setLastStepCount(lastStepCount)
                    stepPrefs.setStartUpBoolean(false)
                }
                val hasPermissionNow = hasPermission.value
                if (hasPermissionNow) {
                    stepCounter.startListening()
                    collectSteps()
                }
            }
        }
    }

    // update daily steps in firestore that have been taken so far
    private fun updateStepsTakenData(userId: String, newSteps: Int) {
        firestoreRepository.updateStepsData(userId, newSteps) { success ->
            if (success) {
                _stepsData.value = _stepsData.value?.copy(dailyStepsTaken = newSteps)
                updateStepsProgress()
            }
        }
    }

    // updates the step goal
    fun updateDailyStepGoal(userId: String, stepGoal: Int) {
        firestoreRepository.updateStepsData(userId, stepGoal = stepGoal) { success ->
            if (success) {
                _stepsData.value = _stepsData.value?.copy(dailyStepGoal = stepGoal)
                updateStepsProgress()
            }
        }
    }

    // resets all steps
    private fun resetStepsForNewDay(userId: String) {
        firestoreRepository.updateStepsData(userId, 0) { success ->
            if (success) {
                lastStepCount = 0
                stepPrefs.setLastStepCount(lastStepCount)
                _stepsData.value = _stepsData.value?.copy(dailyStepsTaken = 0)
                stepCounter.stepCount.value = 0
                updateStepsProgress()
            }
        }
    }

    // gets today's date
    private fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun checkStepPermission() {
        val usingStepDetector = currentlyUsedSensor.value == 1
        _hasPermission.value = if (usingStepDetector) {
            Log.e("StepViewModel", "hasPermission value in checkStepPermission: ${permissions.hasStepDetectorPermission()}")
            permissions.hasStepDetectorPermission()
        } else {
            true
        }
    }

    fun setHasPermission(granted: Boolean) {
        Log.e("StepViewModel", "SET SET SET hasPermission value in setStepPermission: $granted")
        _hasPermission.value = granted
    }

    fun callStartListening() {
        Log.e("StepViewModel", "Start listening from viewmodel called!!!")
        stepCounter.startListening()
    }
}