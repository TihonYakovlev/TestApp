package com.example.testapp.data

import com.example.testapp.data.jsonclasses.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Weather

    @GET("weather")
    suspend fun getWeatherByCoords(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Weather

}
