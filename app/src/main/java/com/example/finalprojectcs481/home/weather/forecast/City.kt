package com.example.finalprojectcs481.home.weather.forecast

import com.example.finalprojectcs481.home.weather.weatherData.Coord
import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("coord") var coord: Coord? = Coord(),
    @SerializedName("country") var country: String? = null,
    @SerializedName("population") var population: Int? = null,
    @SerializedName("timezone") var timezone: Coord? = Coord(),
    @SerializedName("sunrise") var sunrise: Int? = null,
)
