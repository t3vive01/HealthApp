package com.example.healthapplication.ui.screens.shared

import androidx.lifecycle.ViewModel
import com.example.healthapplication.data.models.User
import com.example.healthapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    // used for shared information across different screens

    // -- STATES --
    // -- user data --
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData // can be accessed from UI (holds all user info for specific userId)

    // -- FUNCTIONS --
    fun fetchUserData(userId: String) {
        userRepository.getUserData(userId) { data ->
            _userData.value = data
        }
    }
}