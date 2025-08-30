package com.abelpinheiro.mousephoneapp.ui.trackpad

import android.R.attr.enabled
import android.R.attr.rotation
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TrackpadScreen(viewModel: TrackpadViewModel){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    //TODO might enhance this later for connection drop
    val disabled = false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){
        GyroToggle(
            enabled = uiState.isGyroEnabled,
            onChange = { viewModel.onGyroChanged(it) },
            disabled = disabled
        )

        TrackpadArea(
            onMouseMove = { deltaX, deltaY -> viewModel.onMouseMove(deltaX, deltaY) },
            disabled = disabled || uiState.isGyroEnabled
        )

        MouseButtons(
            onLeftClick = { viewModel.onLeftClick() },
            onRightClick = { viewModel.onRightClick() },
            disabled = disabled
        )
    }
}

@Composable
fun MouseButtons(onLeftClick: () -> Unit, onRightClick: () -> Unit, disabled: Boolean) {
    TODO("Not yet implemented")
}

@Composable
fun TrackpadArea(onMouseMove:  (deltaX: Float, deltaY: Float) -> Unit, disabled: Boolean) {
    TODO("Not yet implemented")
}

@Composable
fun GyroToggle(enabled: Boolean, onChange: (Boolean) -> Unit, disabled: Boolean = false) {
    val infiniteTransition = rememberInfiniteTransition(label = "gyro-spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "gyro-rotation"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.clickable(enabled = !disabled) {onChange(!enabled)}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (enabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.DarkGray),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        Icons.Default.CompassCalibration,
                        contentDescription = "Gyroscope",
                        tint = if (enabled) MaterialTheme.colorScheme.primary else Color.DarkGray,
                        modifier = if (enabled) Modifier.rotate(rotation) else Modifier
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column{
                    Text("Gyroscope Control", fontWeight = FontWeight.Medium, color = Color.White)
                    Text(
                        if (enabled) "Move phone to control cursor" else "Use trackpad for cursor control",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Switch(
                checked = enabled,
                onCheckedChange = onChange,
                enabled = !disabled
            )
        }
    }
}