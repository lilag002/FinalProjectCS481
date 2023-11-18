package com.example.finalprojectcs481.home.homefragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.home.homefragments.weather.CityAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.example.finalprojectcs481.home.homefragments.weather.MyLatLng
import com.example.finalprojectcs481.home.homefragments.weather.WeatherViewModel
import java.util.Calendar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired : Boolean = false
    private val viewModel: WeatherViewModel by viewModels()

//    override fun onResume() {
//        super.onResume()
//        if (locationRequired) startLocationUpdate();
//    }
//    override fun onPause() {
//        super.onPause()
//        locationCallback?.let {
//            fusedLocationProviderClient?.removeLocationUpdates(it)
//        }
//    }
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdate() {
//        locationCallback?.let {
//            val locationRequest = LocationRequest.Builder(
//                Priority.PRIORITY_HIGH_ACCURACY, 100
//            )
//                .setWaitForAccurateLocation(false)
//                .setMinUpdateIntervalMillis(3000)
//                .setMaxUpdateDelayMillis(100)
//                .build()
//            fusedLocationProviderClient?.requestLocationUpdates(
//                locationRequest,
//                it,
//                Looper.getMainLooper()
//            )
//        }
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        initLocationClient()
//    }

//    private fun initLocationClient() {
//        fusedLocationProviderClient = LocationServices
//            .getFusedLocationProviderClient(this)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the current hour
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Determine whether it's day or night
        val isDayTime = currentHour in 6..18

        // Set the background based on the time of day
        view.setBackgroundResource(
            if (isDayTime) {
                R.drawable.weather_morning
            } else {
                R.drawable.weather_night
            }
        )

        val editTextCity: EditText = view.findViewById<EditText>(R.id.editTextCity)
        val buttonAddCity: Button = view.findViewById<Button>(R.id.buttonAddCity)

        // Inside onViewCreated method of WeatherFragment class

        buttonAddCity.setOnClickListener {
            val cityName = editTextCity.text.toString()
            if (cityName.isNotBlank()) {
                // Call the method to fetch weather data
//            fetchWeatherData(cityName)
                viewModel.addCity(cityName)
                // Clear the EditText after adding a city
//                editTextCity.text.clear()
            }
            // You can keep the EditText focused for continuous input
            editTextCity.requestFocus()
        }

        val recyclerViewCities: RecyclerView = view.findViewById(R.id.recyclerViewCities)
        val cityAdapter = CityAdapter()

        viewModel.cities.observe(viewLifecycleOwner) { cities ->
            cityAdapter.submitList(cities)
        }

        recyclerViewCities.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCities.adapter = cityAdapter
    }

//    private fun fetchWeatherData(city: String) {
//        // Retrofit setup for API calls
//        val retrofit = Retrofit.Builder()
//            .baseUrl("YOUR_WEATHER_API_BASE_URL")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val weatherApi = retrofit.create(WeatherApi::class.java)
//
//        // Example: Fetch current weather for the city
//        val call = weatherApi.getCurrentWeather(city, "YOUR_API_KEY")
//
//        call.enqueue(object : Callback<WeatherResponse> {
//            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
//                if (response.isSuccessful) {
//                    val weatherResponse = response.body()
//
//                    // Update your ViewModel with the weather data
//                    viewModel.addCity("$city - ${weatherResponse?.main?.temp}°C")
//                } else {
//                    // Handle error
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
//                // Handle failure
//            }
//        })
//    }

}
