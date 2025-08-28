package com.abelpinheiro.mousephoneapp.ui.home

import android.R.attr.port
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    // Observe the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isConnected){
        // TODO Launch TrackPadScreen
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Mouse Phone") },
                actions = {
                    IconButton(onClick = { /* TODO: Handle settings click */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3A))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.WifiOff,
                            contentDescription = "Disconnected",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Disconnected", color = Color.Gray)
                    }
                    Icon(
                        Icons.Default.PhoneAndroid,
                        contentDescription = "Phone",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show connection error if it exists
            uiState.connectionError?.let { error ->
                ConnectionError(message = error) {
                    viewModel.clearConnectionError()
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Connect to computer section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.PhoneAndroid,
                    contentDescription = "Connect to Computer",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Connect to Computer",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Transform your phone into a wireless\nmouse and trackpad",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { viewModel.onConnectClicked() },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6A5AE0), Color(0xFF946BEE))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Connect Now", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }

    if (uiState.showConnectionDialog) {
        ConnectDialog(
            onDismiss = { viewModel.onDialogDismiss() },
            viewModel = viewModel
        )
    }
}

@Composable
fun ConnectionError(message: String, onDismiss: () -> Unit){
    LaunchedEffect(key1 = message) {
        kotlinx.coroutines.delay(3000) // Show error for 3 seconds
        onDismiss()
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                "Connection Error",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                "Invalid IP address or port",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}