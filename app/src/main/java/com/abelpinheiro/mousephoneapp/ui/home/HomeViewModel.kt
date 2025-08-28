package com.abelpinheiro.mousephoneapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class to hold the entire UI state for the home screen
data class HomeUIState(
    val isConnected: Boolean = false,
    val showConnectionDialog: Boolean = false,
    val connectionError: String? = null
)

class HomeViewModel : ViewModel() {
    // Private mutable state flow that can be updated only within the ViewModel
    private val _uiState = MutableStateFlow(HomeUIState())
    // Public read-only state flow that the UI can observe
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun onConnectClicked() {
        _uiState.update { it.copy(showConnectionDialog = true, connectionError = null) }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(showConnectionDialog = false) }
    }

    fun onAttemptConnection(ip: String, port: String) {
        if(ip.isBlank() || port.isBlank()) {
            _uiState.update {
                it.copy(connectionError = "Invalid IP address or port.")
            }
            return
        }

        // Hide dialog and show loading state if necessary
        _uiState.update { it.copy(showConnectionDialog = false) }

        // --- TODO: Implement actual connection logic here ---
        viewModelScope.launch {
            // Simulate network call
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isConnected = true) }
        }
    }

    fun clearConnectionError() {
        _uiState.update { it.copy(connectionError = null) }
    }
}