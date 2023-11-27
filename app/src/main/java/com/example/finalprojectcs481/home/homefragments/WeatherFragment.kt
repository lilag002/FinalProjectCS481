package com.example.finalprojectcs481.home.homefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions


class WeatherFragment : Fragment() {

    private lateinit var cityName: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//        var cityNameFilled = false
        cityName = view.findViewById(R.id.cityTextView)

//        val loadingIndicator = view.findViewById<ProgressBar>(R.id.loadingIndicator)

        // Determine whether it's day or night
        val isDayTime = currentHour in 6..16

        // Set the background based on the time of day
        view.setBackgroundResource(
            if (isDayTime) {
                R.drawable.weather_morning
            } else {
                R.drawable.weather_night
            }
        )

        // Initially hide the cityTextView
        val cityTextView = view.findViewById<TextView>(R.id.cityText)
        cityTextView.visibility = View.GONE

        // Initialize and execute the task to fetch weather data (coroutine)
        view.findViewById<Button>(R.id.submitBtn).setOnClickListener {
//            loadingIndicator.visibility = View.VISIBLE

            val enteredCity = cityName.text.toString().trim()
            val unitSpinner = view.findViewById<Spinner>(R.id.unitSpinner)
            val selectedUnit = unitSpinner.selectedItem.toString()

            if (enteredCity.isNotEmpty()) {
                // Call fetchweather funct when the button is clicked and cityName is not empty
                // *runs second coroutine due to coroutine funct needing to be inside coroutine body*
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val result = fetchWeatherData(enteredCity)
                        result?.let { json ->
                            // Update your UI with the weather data
                            updateUI(json, selectedUnit)
                        }
                    } catch (e: Exception) {
                        // Handle exceptions (e.g., network errors)
                        e.printStackTrace()
                        Toast.makeText(context, "Error fetching weather data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(context, "Empty city name. Please enter again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        val isRainyDay = true // Will replace with logic to determine if rainy, sunny, etc.

        // Load rain GIF with Glide if it's a rainy day
        if (isRainyDay) {
            val imageView: ImageView = view.findViewById(R.id.imageView)
            Glide.with(this)
                .asGif()
                .load(R.drawable.rain)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(imageView)

        }
    }

    private suspend fun fetchWeatherData(cityName: String): JSONObject? = withContext(Dispatchers.IO) {
        val apiKey = "d6cffea22b14de48943ce7db32244780"
        val apiUrl =
                "https://api.openweathermap.org/data/2.5/weather?q=" +
                "$cityName" +
                "&units=imperial" +
                "&appid=$apiKey"

        try {
            val url = URL(apiUrl)
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line).append('\n')
                }

                return@withContext JSONObject(response.toString())
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    private fun updateUI(json: JSONObject, selectedUnit: String) {
        try {
            // Extract temperature from the JSON response
            val main = json.getJSONObject("main")
            val temperatureValue = main.getDouble("temp")

            // Extract city name from the JSON response
            val cityName = json.getString("name")

            // Extract weather information from the JSON response
            val weatherArray = json.getJSONArray("weather")
            val weatherObject = weatherArray.getJSONObject(0) // Assuming the first item contains the main weather condition
            val weatherCondition = weatherObject.getString("main")
//            val weatherIcon = weatherObject.getString("icon")

            // Example: Update a TextView with the temperature and city name
            val temperatureTextView = view?.findViewById<TextView>(R.id.temperatureTextView)
            val weatherConditionTextView = view?.findViewById<TextView>(R.id.weatherConditionTextView)
            val cityTextView = view?.findViewById<TextView>(R.id.cityText)
//            val weatherIconImageView = view?.findViewById<ImageView>(R.id.weatherIconImageView)

            // Convert temperature based on the selected unit
            val convertedTemperature = if (selectedUnit == "Celsius") {
                // Convert to Celsius and round to two decimal places
                String.format("%.2f", (temperatureValue - 32) * 5 / 9).toDouble()
            } else {
                // Keep it in Fahrenheit and round to two decimal places
                String.format("%.2f", temperatureValue).toDouble()
            }

            val unitSymbol = if (selectedUnit == "Celsius") "C" else "F"
            val temperatureString = "Today's temperature is $convertedTemperature°$unitSymbol"

            // Set the value in the TextView
            temperatureTextView?.text = temperatureString
            // Display the weather condition
            weatherConditionTextView?.text = "Weather: $weatherCondition"

            if (cityName.isNotEmpty()) {
                // Set the city name in the TextView
                cityTextView?.text = cityName
                // Make the TextView visible
                cityTextView?.visibility = View.VISIBLE
            } else {
                // Hide the TextView if the cityName is empty
                cityTextView?.visibility = View.GONE
            }
            // implementation 'com.github.bumptech.glide:glide:4.12.0'
            // annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
            // Then, use Glide to load the weather icon
//            Glide.with(requireContext())
//                .load("https://openweathermap.org/img/wn/$weatherIcon.png")
//                .into(weatherIconImageView!!)

            // Continue extracting and updating other relevant information
            // ...

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}
