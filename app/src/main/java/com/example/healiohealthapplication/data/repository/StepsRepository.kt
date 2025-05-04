package com.example.healthapplication.data.repository

import com.example.healthapplication.data.models.Steps
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class StepsRepository @Inject constructor() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // gets current steps and step goal
    fun getStepData(userId: String, onResult: (Steps?) -> Unit) {
        val stepsCollection = db.collection("users").document(userId).collection("steps")
        stepsCollection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) { // if there are documents in the collection
                    val latestStepsDocument = documents.documents.first()
                    val steps = latestStepsDocument.toObject(Steps::class.java) // convert into Steps data class
                    if (steps != null) {
                        onResult(steps)
                    } else {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


    fun createStepsData(userId: String, dailyStepGoal: Int, onResult: (Boolean) -> Unit) {
        val stepsCollection = db.collection("users").document(userId).collection("steps")
        stepsCollection.get()
            .addOnSuccessListener { documents ->
                val batch = db.batch()
                for (document in documents) {
                    batch.delete(document.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        val steps = Steps(
                            dailyStepsTaken = 0,
                            dailyStepGoal = dailyStepGoal
                        )
                        stepsCollection.add(steps)
                            .addOnSuccessListener { onResult(true) }
                            .addOnFailureListener { onResult(false) }
                    }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener { onResult(false) }
    }


    // updates the daily steps taken so far
    fun updateStepsData(userId: String, stepsTaken: Int? = null, stepGoal: Int? = null, onResult: (Boolean) -> Unit) {
        val stepsCollection = db.collection("users").document(userId).collection("steps")
        stepsCollection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) { // if a document exists
                    val latestStepsDocument = documents.documents.first() // get the first document
                    val updates = mutableMapOf<String, Any>()
                    stepsTaken?.let { updates["dailyStepsTaken"] = it }
                    stepGoal?.let { updates["dailyStepGoal"] = it }
                    latestStepsDocument.reference.update(updates)
                        .addOnSuccessListener { onResult(true) }
                        .addOnFailureListener { onResult(false) }
                } else {
                    onResult(false) // no document found to update
                }
            }
            .addOnFailureListener { onResult(false) }
    }
}