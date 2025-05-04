package com.example.healthapplication.data.repository

import com.example.healthapplication.data.models.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkoutsRepository @Inject constructor(private val db: FirebaseFirestore) {

    // Save a new workout
    suspend fun saveWorkout(userId: String, workout: Workout): Boolean {
        return try {
            val newWorkoutRef = db.collection("users")
                .document(userId)
                .collection("workouts")
                .add(workout)
                .await()

            // After adding, set the generated ID to the workout object
            workout.id = newWorkoutRef.id  // Set the generated ID to workout object
            true // Successfully saved
        } catch (e: Exception) {
            false // Failed to save
        }
    }


    // Get all workouts for a user
    suspend fun getWorkouts(userId: String): List<Workout>? {
        return try {
            val documents = db.collection("users")
                .document(userId)
                .collection("workouts")
                .get()
                .await()

            documents.mapNotNull { doc ->
                val workout = doc.toObject(Workout::class.java)
                workout?.apply { id = doc.id } }
        } catch (e: Exception) {
            null // Failed to retrieve workouts
        }
    }

    // Update an existing workout
    suspend fun updateWorkout(userId: String, workoutId: String, updatedWorkout: Workout): Boolean {
        return try {
            db.collection("users")
                .document(userId)
                .collection("workouts")
                .document(workoutId)
                .set(updatedWorkout)
                .await()
            true // Successfully updated
        } catch (e: Exception) {
            false // Failed to update
        }
    }

    // Delete a workout
    suspend fun deleteWorkout(userId: String, workoutId: String): Boolean {
        return try {
            db.collection("users")
                .document(userId)
                .collection("workouts")
                .document(workoutId)
                .delete()
                .await()
            true // Successfully deleted
        } catch (e: Exception) {
            false // Failed to delete
        }
    }
}
