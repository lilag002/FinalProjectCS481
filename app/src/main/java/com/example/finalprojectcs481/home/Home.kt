package com.example.finalprojectcs481.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.databinding.ActivityHomePageBinding
import com.example.finalprojectcs481.databinding.ActivityMainBinding
import com.example.finalprojectcs481.home.homefragments.HomeFragment
import com.example.finalprojectcs481.home.homefragments.PostFragment
import com.example.finalprojectcs481.home.homefragments.ProfileFragment
import com.example.finalprojectcs481.home.homefragments.SearchFragment
import com.example.finalprojectcs481.home.homefragments.WeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val addPostFragment = PostFragment()
        val weatherFragment = WeatherFragment()
        val userProfileFragment = ProfileFragment()

        changeFragment(homeFragment)


        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.ic_home -> {
                    changeFragment(homeFragment)
                    true
                }

                R.id.ic_search -> {
                    changeFragment(searchFragment)
                    true
                }

                R.id.ic_add -> {
                    changeFragment(addPostFragment)
                    true
                }

                R.id.ic_weather -> {
                    changeFragment(weatherFragment)
                    true
                }

                R.id.ic_profile -> {
                    changeFragment(userProfileFragment)
                    true
                }

                else -> false
            }


        }


    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fContainerView, fragment)
            commit()
        }
    }
}
