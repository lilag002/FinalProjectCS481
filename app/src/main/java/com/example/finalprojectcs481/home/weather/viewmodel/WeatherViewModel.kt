package com.example.finalprojectcs481.home.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {

    private val _cities = MutableLiveData<List<String>>()
    val cities: LiveData<List<String>> get() = _cities

    fun addCity(city: String) {
        val currentCities = _cities.value.orEmpty().toMutableList()
        currentCities.add(city)
        _cities.value = currentCities
    }

}