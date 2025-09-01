package com.abelpinheiro.mousephoneapp.ui.trackpad

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
        gyroscopeManager.startListening()
        gyroscopeJob = viewModelScope.launch {
            gyroscopeManager.gyroscopeData.collectLatest { data ->
                // Map gyroscope data to trackpad movement
                val sensitivity = 15f
                val dx = -data.x * sensitivity
                val dy = -data.y * sensitivity

                // Only send if the movement is significant
                if (dx.toRawBits() != 0 || dy.toRawBits() != 0) {
                    val json = """{"type": "move", "dx": $dx, "dy": $dy}"""
                    connectionRepository.sendMessage(json)
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