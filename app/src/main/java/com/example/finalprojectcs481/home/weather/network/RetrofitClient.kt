package com.example.finalprojectcs481.home.weather.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object{
        private var apiService: serviceAPI? = null
        fun getInstance(): serviceAPI {
            if (apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(serviceAPI::class.java)
            }
            return apiService!!
        }
    }
}