package com.example.healthapplication.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StepPrefs @Inject constructor(@ApplicationContext context: Context) {

    private val stepsStorage = context.getSharedPreferences("step_prefs", Context.MODE_PRIVATE)

    fun setLastResetDate(date: String) {
        stepsStorage.edit().putString("last_reset_date", date).apply()
    }

    fun getLastResetDate(): String? {
        return stepsStorage.getString("last_reset_date", null)
    }

    fun setLastStepCount(count: Int) {
        stepsStorage.edit().putInt("last_step_count", count).apply()
    }

    fun getLastStepCount(): Int = stepsStorage.getInt("last_step_count", 0)

    fun setStartUpBoolean(boolean: Boolean) {
        stepsStorage.edit().putBoolean("boolean", boolean).apply()
    }

    fun getStartUpBoolean(): Boolean {
        return stepsStorage.getBoolean("boolean", true)
    }
}