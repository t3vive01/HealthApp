package com.example.healthapplication.ui.screens.medicine

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.healthapplication.data.models.Medicine
import com.example.healthapplication.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {

    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines: StateFlow<List<Medicine>> = _medicines

    private val _medicine = MutableStateFlow<Medicine?>(null) // To store a single medicine
    val medicine: StateFlow<Medicine?> = _medicine

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    fun setUserId(id: String) {
        _userId.value = id
    }

    // Fetch a single medicine by ID
    fun getMedicineById(id: String, medicineId: String) {
        if (id.isNotEmpty()) {
            repository.getMedicineById(id, medicineId) { medicine ->
                _medicine.value = medicine // Update state flow
            }
        } else {
            _medicine.value = null // Set to null if userId is empty
        }
    }

    // Add a new medicine
    fun addMedicine(medicine: Medicine, onComplete: (Boolean) -> Unit) {
        val uid = _userId.value
        if (uid.isNotEmpty()) {
            repository.addMedicine(uid, medicine) { success ->
                if (success) {
                    fetchMedicines() // Refresh after adding
                }
                onComplete(success)
            }
        } else {
            onComplete(false) // Could not add without a userId
        }
    }

    // Update existing medicine
    fun updateMedicine(medicine: Medicine, onComplete: (Boolean) -> Unit) {
        val uid = _userId.value
        if (uid.isNotEmpty()) {
            repository.updateMedicine(uid, medicine) { success ->
                onComplete(success)
            }
        } else {
            onComplete(false)
        }
    }


    // Delete a medicine
    fun deleteMedicine(medicineId: String, onComplete: (Boolean) -> Unit) {
        val uid = _userId.value
        if (uid.isNotEmpty()) {
            repository.deleteMedicine(uid, medicineId) { success ->
                if (success) {
                    fetchMedicines() // Refresh after deletion
                }
                onComplete(success)
            }
        } else {
            onComplete(false) // Handle the case when userId is empty
        }
    }

    // Fetch all medicines
    fun fetchMedicines() {
        val uid = _userId.value
        if (uid.isNotEmpty()) {
            repository.getMedicines(uid) { fetchedMedicines ->
                _medicines.value = fetchedMedicines
            }
        }
    }

}


