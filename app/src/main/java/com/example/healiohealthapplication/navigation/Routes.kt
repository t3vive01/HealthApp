package com.example.healthapplication.navigation

// the routes used in navigation throughout the application
object Routes {
    const val START = "start"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val USER = "user"
    const val EDIT_USER = "edit_user"
    const val DIET = "diet"
    const val STEPS = "steps"
    const val WORKOUT = "workout"
    const val EDIT_WORKOUT = "edit_workout/{workoutName}/{workoutDuration}"
    const val VIEW_PROGRESS = "view_progress/{workouts}"
    const val ADD_WORKOUT = "add_workout"
    const val MEDICINE = "medicine"
    const val MEDICINE_LIST = "medicine_list"
    const val MEDICINE_DETAIL = "medicine_detail/{medicineName}/{description}/{schedule}/{amount}/{duration}"
    const val ADD_MEDICINE = "add_medicine"
    const val CALENDAR = "calendar"
    const val WELCOME = "welcome"
}