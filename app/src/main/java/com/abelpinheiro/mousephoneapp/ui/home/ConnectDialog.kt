package com.abelpinheiro.mousephoneapp.ui.home

import android.R.attr.port
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ConnectDialog(viewModel: HomeViewModel, onDismiss: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2D))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(Icons.Default.Wifi, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connect to Computer", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss){
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
                Text(
                    "Enter your computer's IP address and port to establish a connection.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF2A2A3A),
                    contentColor = Color.White,
                    indicator = {},
                    divider = {}
                ){
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.padding(4.dp).background(if (selectedTab == 0) Color(0xFF3A3A4A) else Color.Transparent, RoundedCornerShape(8.dp))
                    ) {
                        Text("Manual Entry", modifier = Modifier.padding(vertical = 12.dp))
                    }
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.padding(4.dp).background(if (selectedTab == 1) Color(0xFF3A3A4A) else Color.Transparent, RoundedCornerShape(8.dp))
                    ) {
                        Text("QR Scan", modifier = Modifier.padding(vertical = 12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Content based on tab
                if(selectedTab == 0)
                {
                    ManualEntryContent(
                        uiState = uiState,
                        onIpAddressChange = { viewModel.onIpAddressChanged(it) },
                        onPortChange = { viewModel.onPortChanged(it) },
                        onCancel = onDismiss,
                        onConnect = { viewModel.onAttemptConnection() }
                    )
                }
                else{
                    QrScanContent(onCancel = onDismiss)
                }
            }
        }
    }
}

    @Composable
    fun ManualEntryContent(
        uiState: HomeUIState,
        onIpAddressChange: (String) -> Unit,
        onPortChange: (String) -> Unit,
        onCancel: () -> Unit,
        onConnect: () -> Unit
    ){
        Column{
            Text("IP Address", color = Color.Gray)
            OutlinedTextField(
                value = uiState.ipAddress,
                onValueChange = onIpAddressChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = IpAddressVisualTransformation(),
                isError = uiState.ipAddressError != null,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )
            uiState.ipAddressError?.let{
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Port", color = Color.Gray)
            OutlinedTextField(
                value = uiState.port,
                onValueChange = onPortChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.portError != null,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )
            uiState.portError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                OutlinedButton(onClick = onCancel, shape = RoundedCornerShape(50)) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onConnect,
                    enabled = uiState.isConnectButtonEnabled,
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6A5AE0), Color(0xFF946BEE))
                                )
                            ).padding(horizontal = 24.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Connect", color = Color.White)
                    }
                }
            }
        }
    }

    @Composable
    fun QrScanContent(onCancel: () -> Unit){
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = "Scan QR Code",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Scan QR Code", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(
                "Scan the QR code displayed on your computer to connect automatically",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Handle QR Scan */ },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(0.9f).height(50.dp),
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
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start QR Scan", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onCancel, shape = RoundedCornerShape(50)) {
                Text("Cancel")
            }
        }
    }

// VisualTransformation for IP formatting
class IpAddressVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 12) text.text.substring(0..11) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 3 == 2 && i < 11) out += "."
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                if (offset <= 11) return offset + 3
                return 15
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                if (offset <= 11) return offset - 2
                if (offset <= 15) return offset - 3
                return 12
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
