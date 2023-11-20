package com.example.finalprojectcs481.home.weather.weatherData

import com.google.gson.annotations.SerializedName

data class Weather (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("main") var main: String? = null,
    @SerializedName("description") var description: Int? = null,
    @SerializedName("icon") var icon: String? = null,
)