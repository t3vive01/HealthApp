package com.example.healthapplication.data.models

data class User (
    var id: String = "",
    var email: String = "",
    var weight: String = "--",
    var height: String = "--",
    var age: String = "--",
    var gender: String = "--",
    var bmi: String = "--"
)