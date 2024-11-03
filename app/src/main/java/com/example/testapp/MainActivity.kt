package com.example.testapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.viewmodels.WeatherViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val navController = rememberNavController()
            val weatherViewModel: WeatherViewModel by viewModels()
            TestAppTheme {
                WeatherScreen(weatherViewModel)
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weather by viewModel.weather
    Log.d("WeatherScreen", "Current weather: $weather")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.fetchWeather("London") }) {
            Text(text = "Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        weather?.let {
            Text(text = "City: ${it.name}")
            Text(text = "Temperature: ${it.main.temp}")
            Text(text = "Feels Like: ${it.main.feels_like}")
        }
    }
}



