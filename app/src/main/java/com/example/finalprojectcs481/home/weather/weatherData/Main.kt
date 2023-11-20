package com.example.finalprojectcs481.home.weather.weatherData

import com.google.gson.annotations.SerializedName

data class Main (
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("temp_min") var tempMin: Double? = null,
    @SerializedName("temp_max") var tempMax: Double? = null,
    @SerializedName("pressure") var pressure: Double? = null,
    @SerializedName("humidity") var humidity: Double? = null
)