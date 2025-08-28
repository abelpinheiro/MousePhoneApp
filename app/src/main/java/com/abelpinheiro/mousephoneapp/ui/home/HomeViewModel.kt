package com.abelpinheiro.mousephoneapp.ui.home

import android.R.attr.port
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
    val connectionError: String? = null,
    val ipAddress: String = "",
    val port: String = "",
    val ipAddressError: String? = null,
    val portError: String? = null,
    val isConnectButtonEnabled: Boolean = false
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

    /**
     * Updates the IP address input in the UI state and validates the input.
     */
    fun onIpAddressChanged(ip: String){
        val filteredIp = ip.filter { it.isDigit() }.take(12)
        _uiState.update { it.copy(ipAddress = filteredIp) }
        validateInputs()
    }

    /**
     * Updates the port input in the UI state and validates the input.
     */
    fun onPortChanged(port: String){
        val filteredPort = port.filter { it.isDigit() }.take(5)
        _uiState.update { it.copy(port = filteredPort) }
        validateInputs()
    }

    fun onAttemptConnection() {
        validateInputs()
        val currentState = _uiState.value
        if(!currentState.isConnectButtonEnabled) return

        // Hide dialog and show loading state if necessary
        _uiState.update { it.copy(showConnectionDialog = false) }

        // --- TODO: Implement actual connection logic here ---
        viewModelScope.launch {
            // Simulate network call
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isConnected = true) }
        }
    }

    /*
     * Validates the IP address and port inputs and updates the UI state accordingly.
     */
    private fun validateInputs() {
        val ip = _uiState.value.ipAddress
        val port = _uiState.value.port

        // IP Validation
        val ipError = if (ip.isBlank()) {
            "IP address cannot be empty"
        } else if (ip.split(".").any { it.toIntOrNull()?.let { num -> num > 255 } == true }) {
            "Invalid IP address segment"
        } else {
            null
        }

        // Port Validation
        val portError = if (port.isBlank()) {
            "Port cannot be empty"
        } else if (port.toIntOrNull()?.let { it < 1 || it > 65535 } == true) {
            "Port must be between 1 and 65535"
        } else {
            null
        }

        _uiState.update {
            it.copy(
                ipAddressError = ipError,
                portError = portError,
                isConnectButtonEnabled = ipError == null && portError == null
            )
        }
    }

    fun clearConnectionError() {
        _uiState.update { it.copy(connectionError = null) }
    }
}