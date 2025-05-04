package com.example.healthapplication.data.repository

import android.util.Log
import com.example.healthapplication.data.models.Medicine
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class MedicineRepository @Inject constructor() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun addMedicine(userId: String, medicine: Medicine, onResult: (Boolean) -> Unit) {
        val collection = db.collection("users").document(userId).collection("medicine")
        val document = collection.document()
        val medicineWithId = medicine.copy(id = document.id)
        document.set(medicineWithId)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    // Fetch all medicines for a specific user
    fun getMedicines(userId: String, onResult: (List<Medicine>) -> Unit) {
        db.collection("users").document(userId)
            .collection("medicine")
            .get()
            .addOnSuccessListener { snapshot ->
                val medicines = snapshot.documents.mapNotNull { it.toObject<Medicine>() }
                onResult(medicines)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    // Fetch a specific medicine by its ID
    fun getMedicineById(userId: String, medicineId: String, onResult: (Medicine?) -> Unit) {
        db.collection("users").document(userId)
            .collection("medicine").document(medicineId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val medicine = document.toObject<Medicine>()
                    onResult(medicine)
                    Log.d("MedicineRepository", "Medicine fetched by id")
                } else {
                    onResult(null) // Medicine not found
                }
            }
            .addOnFailureListener {
                onResult(null) // Return null in case of failure
                // Optionally log the error for better debugging
                Log.e("MedicineRepository", "Failed to fetch medicine by id", it)
            }
    }

    // Update a specific medicine
    fun updateMedicine(userId: String, medicine: Medicine, onResult: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .collection("medicine").document(medicine.id ?: "")
            .set(medicine)
            .addOnSuccessListener {
                onResult(true)
                Log.d("MedicineRepository", "Medicine updated in firestore")
            }
            .addOnFailureListener {
                onResult(false)
                // Optionally log the error for better debugging
                Log.e("MedicineRepository", "Failed to update medicine", it)
            }
    }

    // Delete a specific medicine
    fun deleteMedicine(userId: String, medicineId: String, onResult: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .collection("medicine").document(medicineId)
            .delete()
            .addOnSuccessListener {
                onResult(true)
                Log.d("MedicineRepository", "Medicine deleted from firestore")
            }
            .addOnFailureListener {
                onResult(false)
                // Optionally log the error for better debugging
                Log.e("MedicineRepository", "Failed to delete medicine", it)
            }
    }
}
