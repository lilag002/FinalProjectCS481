package com.example.finalprojectcs481.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.finalprojectcs481.R
import com.google.firebase.auth.FirebaseAuth
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val backButton = findViewById<Button>(R.id.backButton)
        val loginButton = findViewById<Button>(R.id.loginBtn)

        backButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Close the current LoginActivity
        }

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.insert_email).text
            val password = findViewById<EditText>(R.id.insert_password).text
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this@LoginActivity, "Please fill out missing fields", Toast.LENGTH_SHORT).show()
            }
            else{
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email.toString().trim(), password.toString().trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                            // Navigate to the next activity/fragment or do whatever you want upon successful login
                        } else {
                            Toast.makeText(this@LoginActivity, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }
}