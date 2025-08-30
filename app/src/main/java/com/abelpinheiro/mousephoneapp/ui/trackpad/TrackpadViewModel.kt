package com.abelpinheiro.mousephoneapp.ui.trackpad

import androidx.lifecycle.ViewModel
import com.abelpinheiro.mousephoneapp.data.ConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TrackpadUiState(
    val isGyroEnabled: Boolean = false
)

class TrackpadViewModel(
    private val connectionRepository: ConnectionRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TrackpadUiState())
    val uiState: StateFlow<TrackpadUiState> = _uiState.asStateFlow()

    fun onGyroChanged(isEnabled: Boolean){
        _uiState.update { it.copy(isGyroEnabled = isEnabled) }
        // TODO: Add logic to start/stop listening to gyroscope sensors
    }

    fun onMouseMove(deltaX: Float, deltaY: Float){
        // TODO: Send mouse move data to the server
        println("Mouse moved by: ($deltaX, $deltaY)")
    }

    fun onLeftClick(){
        //TODO Send Left click event
        println("Left Click")
    }

    fun onRightClick(){
        //TODO Send right click event
        println("Right Click")
    }
}