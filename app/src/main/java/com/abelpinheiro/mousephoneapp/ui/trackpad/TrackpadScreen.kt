package com.abelpinheiro.mousephoneapp.ui.trackpad

import android.R.attr.track
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerType.Companion.Mouse
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abelpinheiro.mousephoneapp.ui.components.ConnectionStatusCard
import com.abelpinheiro.mousephoneapp.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackpadScreen(homeViewModel: HomeViewModel, trackpadViewModel: TrackpadViewModel){
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val trackpadUiState  by trackpadViewModel.uiState.collectAsStateWithLifecycle()
    //TODO might enhance this later for connection drop
    val disabled = false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Mouse") },
                navigationIcon = {
                    IconButton(onClick = { trackpadViewModel.onDisconnectButtonClicked() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = "Disconnect")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            ConnectionStatusCard(uiState = homeUiState)

            Spacer(modifier = Modifier.height(16.dp))

            GyroToggle(
                enabled = trackpadUiState .isGyroEnabled,
                onChange = { trackpadViewModel.onGyroChanged(it) },
                disabled = disabled
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Trackpad", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(8.dp))

            TrackpadArea(
                modifier = Modifier.fillMaxWidth().weight(1f),
                onMouseMove = { deltaX, deltaY -> trackpadViewModel.onMouseMove(deltaX, deltaY) },
                disabled = trackpadUiState .isGyroEnabled
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Mouse Buttons", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(8.dp))

            MouseButtons(
                onLeftClick = { trackpadViewModel.onLeftClick() },
                onRightClick = { trackpadViewModel.onRightClick() },
                disabled = disabled
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { trackpadViewModel.onDisconnectButtonClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB71C1C),
                    contentColor = Color.White
                )
            ) {
                Text("Disconnect", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun MouseButtons(onLeftClick: () -> Unit, onRightClick: () -> Unit, disabled: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onLeftClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ){
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Left Click",
                modifier = Modifier.rotate(-45f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Left")
        }

        Button(
            onClick = onRightClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ){
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Right Click",
                modifier = Modifier.rotate(45f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Right")
        }
    }
}

@Composable
fun TrackpadArea(modifier: Modifier, onMouseMove:  (deltaX: Float, deltaY: Float) -> Unit, disabled: Boolean) {
    val trackpadColor = if (disabled) MaterialTheme.colorScheme.surface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(trackpadColor)
            .pointerInput(disabled){
                if(!disabled){
                    // Detect drag gestures on the trackpad
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onMouseMove(dragAmount.x, dragAmount.y)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ){
        if (disabled){
            Text("Trackpad disabled (Gyro is ON)", color = Color.White.copy(alpha = 0.7f))
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mouse,
                    contentDescription = "Mouse Icon",
                    tint = Color.Gray
                )
                Text(
                    "Touch to move cursor",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Drag to control mouse movement",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
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