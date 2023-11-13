package com.example.finalprojectcs481.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import java.util.*

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
        val rootView = inflater.inflate(R.layout.fragment_weather, container, false)

        val mainContainer = rootView.findViewById<RelativeLayout>(R.id.mainContainer)

        updateBackground(mainContainer)

        return rootView
    }

    private fun updateBackground(layout: RelativeLayout){
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)

        val drawableResourceId = if(hour >= 20 || hour < 6) {
            R.drawable.weather_bg
        } else {
            R.drawable.weather_day
        }

        layout.setBackgroundResource(drawableResourceId)
    }

}