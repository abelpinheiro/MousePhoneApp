package com.abelpinheiro.mousephoneapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abelpinheiro.mousephoneapp.ui.home.HomeUIState

@Composable
fun ConnectionStatusCard(uiState: HomeUIState){
    val isConnected = uiState.isConnected
    val cardColor = if (isConnected) Color(0xFF192E24) else Color(0xFF2A2A3A)
    val icon = if (isConnected) Icons.Default.Wifi else Icons.Default.WifiOff
    val iconColor = if (isConnected) Color(0xFF2F845C) else Color.Gray
    val text = if (isConnected) "Connected" else "Disconnected"
    val textColor = if (isConnected) Color(0xFF2F845C) else Color.Gray

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = iconColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text, color = textColor, fontWeight = FontWeight.Bold)
                    if (isConnected) {
                        Text("Desktop Computer", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
            Icon(
                Icons.Default.PhoneAndroid,
                contentDescription = "Phone",
                tint = Color.Gray
            )
        }
    }
}