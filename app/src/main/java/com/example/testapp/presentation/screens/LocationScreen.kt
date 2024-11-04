package com.example.testapp.presentation.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.testapp.BottomBarRoutes
import com.example.testapp.viewmodels.WeatherViewModel
import java.text.DecimalFormatSymbols

@Composable
fun LocationScreen(
    viewModel: WeatherViewModel,
    navHostController: NavHostController,
    context: Activity
) {
    val weather by viewModel.weather
    var longitude by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var openAlertDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {

        IconButton(
            onClick = {
                openAlertDialog = true
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Info",
                modifier = Modifier.size(40.dp)
            )
        }

        if (openAlertDialog) {
            MinimalDialog(onDismiss = { openAlertDialog = false })
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier,
                value = latitude,
                onValueChange = { newLatitude ->
                    latitude = DecimalFormatter().cleanup(newLatitude)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(text = "Latitude", color = Color.LightGray) },
            )

            OutlinedTextField(
                modifier = Modifier,
                value = longitude,
                onValueChange = { newLongitude ->
                    longitude = DecimalFormatter().cleanup(newLongitude)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(text = "Longitude", color = Color.LightGray) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                viewModel.fetchWeatherByCoords(
                    longitude,
                    latitude
                )
            }) { Text("Get weather") }

            weather?.let {
                Text(text = "City: ${it.name}")
                Text(text = "Temperature: ${it.main.temp.toInt()}°C")
                Text(text = "Feels Like: ${it.main.feels_like.toInt()}°C")
            }
        }
    }

    BackHandler {
        navHostController.navigate(BottomBarRoutes.CITY.routes) {
            popUpTo(BottomBarRoutes.CITY.routes) {
                inclusive = true
            }
        }
    }
}

@Composable
fun MinimalDialog(onDismiss: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    private val decimalSeparator = symbols.decimalSeparator
    fun cleanup(input: String): String {

        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return "0"

        val sb = StringBuilder()

        var hasDecimalSep = false

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
            if (char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty()) {
                sb.append(char)
                hasDecimalSep = true
            }
        }

        return sb.toString()
    }
}