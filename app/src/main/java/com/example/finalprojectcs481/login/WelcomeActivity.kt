package com.example.finalprojectcs481.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.home.Home
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // If a user is already logged in, proceed to the next activity
            startActivity(Intent(this, Home::class.java))
            finish()
        }

        val loginPage = findViewById<Button>(R.id.loginBtn)
        loginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val signupPage = findViewById<Button>(R.id.signupBtn)
        signupPage.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    //BIG BNOOts
}