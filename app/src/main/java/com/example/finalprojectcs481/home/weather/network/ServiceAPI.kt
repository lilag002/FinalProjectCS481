package com.example.finalprojectcs481.home.weather.network



import com.example.finalprojectcs481.home.weather.forecast.ForecastResult
import com.example.finalprojectcs481.home.weather.weatherData.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface serviceAPI {
    @GET("weather")
    suspend fun  getWeather(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lng: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appId") appId: String = "d6cffea22b14de48943ce7db32244780",
    ): WeatherResult

    @GET("forecast")
    suspend fun  getForecast(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lng: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appId") appId: String = "d6cffea22b14de48943ce7db32244780",
    ): ForecastResult
}