package com.example.healthapplication.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.math.sqrt

// tracks steps using the accelerometer or android step counter sensor depending on what the phone has
class StepCounter @Inject constructor(
    @ApplicationContext context: Context,
    private val stepPrefs: StepPrefs,
    private val stepPermissions: Permissions
) : SensorEventListener { // implements the sensor event listener (context is the way the sensor is accessed)
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepDetector: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val stepCount = MutableStateFlow(0)
    val currentlyUsedSensor = MutableStateFlow(0) // 1 = step detector, 2 = accelerometer
    val isStepTrackingSupported = MutableStateFlow(true)

    private var previousMagnitude = 0f
    private var stepThreshold = 11f
    private var stepCooldown = 0L
    private val stepDelay = 300L // milliseconds between valid steps

    fun startListening() {
        stepPrefs.setStartUpBoolean(true)
        if (stepDetector == null) {
            if (accelerometer != null) {
                accelerometer?.let {
                    currentlyUsedSensor.value = 2
                    sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
                }
            } else {
                Log.e("StepCounter", "No sensor of type accelerometer or step detector found! Unable to count steps.")
                isStepTrackingSupported.value = false
            }
        } else {
            currentlyUsedSensor.value = 1
            val hasPermission = stepPermissions.hasStepDetectorPermission()
            Log.e("StepCounter", "hasPermission value: $hasPermission") // comes as "false" even after being set to true
            if (hasPermission) {
                Log.e("StepCounter", "started listening to steps using step detector")
                stepDetector?.let { // only listens if there is a step detector in the phone
                    // "this" refers to the current stepCounter instance. this is where updates of listening are sent to
                    // "it" refers to the stepSensor which will be sending the updates
                    // "SensorManager.SENSOR_DELAY_UI determines how fast the updates are given
                    sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
                }
            }
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) { // triggers whenever there is a new sensor event
        if (currentlyUsedSensor.value == 1) {
            if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) { // if an event from specifically the step detector is noticed then
                val newStepValue = stepCount.value + 1
                stepCount.value = newStepValue // add a step (one event = one step)
            }
        } else if (currentlyUsedSensor.value == 2) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val magnitude = sqrt(x * x + y * y + z * z)

                val currentTime = System.currentTimeMillis()
                if (magnitude > stepThreshold && previousMagnitude <= stepThreshold &&
                    currentTime - stepCooldown > stepDelay
                ) {
                    stepCount.value += 1
                    stepCooldown = currentTime
                }

                previousMagnitude = magnitude
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // the "SensorEventListener" requires this function
    }
}