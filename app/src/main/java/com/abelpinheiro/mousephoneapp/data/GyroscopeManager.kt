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
    private val gyroscopeSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val _gyroscopeData = MutableStateFlow(GyroscopeData(0f, 0f, 0f))
    val gyroscopeData: StateFlow<GyroscopeData> = _gyroscopeData.asStateFlow()

    fun startListening() {
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            _gyroscopeData.value = GyroscopeData(
                x = event.values[0],
                y = event.values[1],
                z = event.values[2]
            )
        }
    }

}