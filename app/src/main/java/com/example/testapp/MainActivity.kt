package com.example.testapp

import android.os.Bundle
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val city = remember{mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = city.value, onValueChange = { newText -> city.value = newText},  )

        Button(onClick = { viewModel.fetchWeather(city.value) }) {
            Text(text = "Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        weather?.let {
            Text(text = "City: ${it.name}")
            Text(text = "Temperature: ${it.main.temp.toInt()}°C")
            Text(text = "Feels Like: ${it.main.feels_like.toInt()}°C")
        }
    }
}



