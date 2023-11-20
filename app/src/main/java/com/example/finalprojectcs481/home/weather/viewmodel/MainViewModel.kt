package com.example.finalprojectcs481.home.weather.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectcs481.home.weather.MyLatLng
import com.example.finalprojectcs481.home.weather.forecast.ForecastResult
import com.example.finalprojectcs481.home.weather.network.RetrofitClient
import com.example.finalprojectcs481.home.weather.weatherData.WeatherResult
import com.google.type.LatLng
import kotlinx.coroutines.launch

enum class STATE {
    LOADING,
    SUCCESS,
    FAILURE
}
class MainViewModel : ViewModel(){
    //control state of view model
    var state by mutableStateOf(STATE.LOADING)
    //hold value from api for weather info
    var weatherResponse : WeatherResult by mutableStateOf(WeatherResult())
    //hold value from api for forecast info
    var forecastResponse: ForecastResult by mutableStateOf(ForecastResult())

    var errorMessage: String by mutableStateOf("")

    fun getWeatherByLocation(LatLng: MyLatLng){
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.getWeather(LatLng.lat, LatLng.lng)
                weatherResponse = apiResponse
                state = STATE.SUCCESS
            }catch(e: Exception){
                var errorMessage = e.message!!.toString()
                state = STATE.FAILURE
            }
        }
    }
    fun getForecastByLocation(LatLng: MyLatLng){
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.getForecast(LatLng.lat, LatLng.lng)
                forecastResponse = apiResponse
                state = STATE.SUCCESS
            }catch(e: Exception){
                var errorMessage = e.message!!.toString()
                state = STATE.FAILURE
            }
        }
    }
}