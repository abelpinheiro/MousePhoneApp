package com.abelpinheiro.mousephoneapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abelpinheiro.mousephoneapp.data.ConnectionRepository
import com.abelpinheiro.mousephoneapp.data.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Data class to hold the entire UI state for the home screen
data class HomeUIState(
    val isConnected: Boolean = false,
    val showConnectionDialog: Boolean = false,
    val connectionError: String? = null,
    val ipAddress: String = "",
    val port: String = "",
    val ipAddressError: String? = null,
    val portError: String? = null,
    val isConnectButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectionRepository: ConnectionRepository
) : ViewModel() {
    // Private mutable state flow that can be updated only within the ViewModel
    private val _uiState = MutableStateFlow(HomeUIState())
    // Public read-only state flow that the UI can observe
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            connectionRepository.connectionState.collectLatest { state ->
                when(state){
                    is ConnectionState.Connected -> { _uiState.update { it.copy(isConnected = true, isLoading = false) } }
                    is ConnectionState.Connecting -> { _uiState.update { it.copy(isLoading = true, connectionError = null) } }
                    is ConnectionState.Error -> { _uiState.update { it.copy(isLoading = false, connectionError = state.message) } }
                    is ConnectionState.Disconnected -> { _uiState.update { it.copy(isConnected = false, isLoading = false) } }
                    else -> _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

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
        if(ip.length <= 15) {
            _uiState.update { it.copy(ipAddress = ip) }
            validateInputs()
        }
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

        // Initiate connection
        connectionRepository.connect(currentState.ipAddress, currentState.port)
        _uiState.update { it.copy(showConnectionDialog = false) }
    }

    /*
     * Validates the IP address and port inputs and updates the UI state accordingly.
     */
    private fun validateInputs() {
        val ip = _uiState.value.ipAddress
        val port = _uiState.value.port

        // IP Validation
        // Regex to validate IPV4
        val ipRegex = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")

        val ipError = if (ip.isBlank()) {
            "IP address cannot be empty"
        } else if (!ip.matches(ipRegex)) {
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