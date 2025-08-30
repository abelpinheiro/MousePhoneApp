package com.abelpinheiro.mousephoneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abelpinheiro.mousephoneapp.data.ConnectionRepositoryImpl
import com.abelpinheiro.mousephoneapp.data.ViewModelFactory
import com.abelpinheiro.mousephoneapp.data.WebSocketDataSource
import com.abelpinheiro.mousephoneapp.ui.home.HomeScreen
import com.abelpinheiro.mousephoneapp.ui.home.HomeViewModel
import com.abelpinheiro.mousephoneapp.ui.theme.MousePhoneAppTheme
import com.abelpinheiro.mousephoneapp.ui.trackpad.TrackpadViewModel

class MainActivity : ComponentActivity() {
    val webSocketDataSource = WebSocketDataSource()
    val connectionRepository = ConnectionRepositoryImpl(webSocketDataSource)
    val viewModelFactory = ViewModelFactory(connectionRepository)

    val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    val trackpadViewModel: TrackpadViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MousePhoneAppTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        trackpadViewModel = trackpadViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = Color.Red) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayCardPreview() {
    MousePhoneAppTheme {
        Greeting("Jack")
    }
}