package com.example.finalprojectcs481.home.homefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    }
}
