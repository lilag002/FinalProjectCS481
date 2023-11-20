package com.example.finalprojectcs481.home.weather.weatherData

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") var all: Int? = null
)
