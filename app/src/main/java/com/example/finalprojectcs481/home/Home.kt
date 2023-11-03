package com.example.finalprojectcs481.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.home.homefragments.Add_Post
import com.example.finalprojectcs481.home.homefragments.Home_Page
import com.example.finalprojectcs481.home.homefragments.Search_Page
import com.example.finalprojectcs481.home.homefragments.User_Profile
import com.example.finalprojectcs481.home.homefragments.Weather_Page
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        val homeFragment = Home_Page()
        val searchFragment = Search_Page()
        val addPostFragment = Add_Post()
        val weatherFragment = Weather_Page()
        val userProfileFragment = User_Profile()

        changeFragment(homeFragment)

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnItemSelectedListener { item ->
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
