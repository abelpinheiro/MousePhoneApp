package com.abelpinheiro.mousephoneapp.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class GyroscopeData(val x: Float, val y: Float, val z: Float)

@Singleton
class GyroscopeManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val _orientationAngles = MutableStateFlow(FloatArray(3))
    val orientationAngles: StateFlow<FloatArray> = _orientationAngles.asStateFlow()

    fun startListening() {
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            val orientationAngles = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            _orientationAngles.value = orientationAngles
        }
    }

}