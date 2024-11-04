package com.example.testapp.presentation.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testapp.ScreenRoutes
import com.example.testapp.viewmodels.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel, navHostController: NavHostController,
    context: Activity
) {
    val weather by viewModel.weather
    val city = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Box(contentAlignment = Alignment.Center) {
            IconButton(onClick = {
                navHostController.navigate(ScreenRoutes.Detail.route)
            }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Details", modifier = Modifier.size(80.dp),
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = city.value,
                onValueChange = { newText -> city.value = newText },
                singleLine = true,
                label = { Text(text = "City", color = Color.LightGray) },
                )
            Spacer(modifier = Modifier.height(20.dp))


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

        BackHandler {
            context.finish()
        }
    }
}