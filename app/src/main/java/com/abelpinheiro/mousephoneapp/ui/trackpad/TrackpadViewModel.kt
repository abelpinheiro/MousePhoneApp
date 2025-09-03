package com.abelpinheiro.mousephoneapp.ui.trackpad

import android.R.attr.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelpinheiro.mousephoneapp.data.ConnectionRepository
import com.abelpinheiro.mousephoneapp.data.GyroscopeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

/**
 * UI state for the trackpad screen
 */
data class TrackpadUiState(
    val isGyroEnabled: Boolean = false
)

/**
 * ViewModel for the trackpad screen
 */
@HiltViewModel
class TrackpadViewModel @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    private val gyroscopeManager: GyroscopeManager
): ViewModel() {
    private val _uiState = MutableStateFlow(TrackpadUiState())
    val uiState: StateFlow<TrackpadUiState> = _uiState.asStateFlow()
    private var gyroscopeJob: Job? = null
    private var initialOrientation: FloatArray? = null

    /**
     * Toggle gyroscope sensor on/off
     */
    fun onGyroChanged(isEnabled: Boolean){
        _uiState.update { it.copy(isGyroEnabled = isEnabled) }
        if (isEnabled) {
            startGyroscope()
        } else {
            stopGyroscope()
        }
    }

    /**
     * Stop listening to gyroscope data
     */
    private fun stopGyroscope() {
        gyroscopeManager.stopListening()
        gyroscopeJob?.cancel()
    }

    /**
     * Start listening to gyroscope data and map it to trackpad movement
     */
    private fun startGyroscope() {
        initialOrientation = null
        gyroscopeManager.startListening()
        gyroscopeJob = viewModelScope.launch {
            gyroscopeManager.orientationAngles.collectLatest { currentAngles ->
                // If we don't have initial orientation, set it
                if (initialOrientation == null) {
                    initialOrientation = currentAngles
                }

                val initial = initialOrientation ?: return@collectLatest

                // Calculate the difference between the current and initial orientation
                val roll = currentAngles[2] - initial[2]
                val pitch = currentAngles[1] - initial[1]

                // Map the gyroscope data to trackpad movement
                val sensitivity = 250f
                val dx = roll * sensitivity
                val dy = pitch * sensitivity

                // Send the trackpad movement to the server
                if (abs(dx) > 0.1 || abs(dy) > 0.1) {
                    val moveJson = """{"type": "move", "dx": $dx, "dy": $dy}"""
                    connectionRepository.sendMessage(moveJson)
                }
            }
        }
    }

    /**
     * Send mouse move event to server
     */
    fun onMouseMove(deltaX: Float, deltaY: Float){
        val json = """{"type": "move", "dx": $deltaX, "dy": $deltaY}"""
        connectionRepository.sendMessage(json)
    }

    /**
     * Send left click event to server
     */
    fun onLeftClick(){
        val json = """{"type": "click", "button": "left"}"""
        connectionRepository.sendMessage(json)
    }

    /**
     * Send right click event to server
     */
    fun onRightClick(){
        val json = """{"type": "click", "button": "right"}"""
        connectionRepository.sendMessage(json)
    }

    /**
     * Disconnect from server
     */
    fun onDisconnectButtonClicked() {
        connectionRepository.disconnect()
    }

    /**
     * Called when the ViewModel is no longer used and will be destroyed
     */
    override fun onCleared() {
        super.onCleared()
        connectionRepository.disconnect()
        println("TrackpadViewModel cleared, connection closed.")
    }
}