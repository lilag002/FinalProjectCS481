package com.example.finalprojectcs481.home.weather.forecast

import com.example.finalprojectcs481.home.weather.weatherData.Clouds
import com.example.finalprojectcs481.home.weather.weatherData.Sys
import com.example.finalprojectcs481.home.weather.weatherData.Weather
import com.example.finalprojectcs481.home.weather.weatherData.Wind
import com.google.gson.annotations.SerializedName

data class CustomList(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("main") var main: Int? = null,
    @SerializedName("weather") var weather: ArrayList<Weather>? = arrayListOf(),
    @SerializedName("clouds") var clouds: Clouds? = Clouds(),
    @SerializedName("wind") var wind: Wind? = Wind(),
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("sys") var sys: Sys? = Sys(),
    @SerializedName("dt_text") var dtText: City? = City(),
)
