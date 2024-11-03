package com.example.testapp.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.RetrofitInstance
import com.example.testapp.data.jsonclasses.Weather
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val apiKey = "10a9467071025c0f6f1aa1c554e27723"
    private val units = "metric"

    private val _weather = mutableStateOf<Weather?>(null)
    val weather: State<Weather?> = _weather

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {

                val response = RetrofitInstance.api.getWeather(
                    city,
                    apiKey,
                    units
                )

                _weather.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}