package com.example.finalprojectcs481

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.finalprojectcs481.fragments.HomeFragment
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val backButton = findViewById<Button>(R.id.backButton)
        val signupButton = findViewById<Button>(R.id.submit_button)

        backButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Close the current LoginActivity
        }

//        val emailPattern = Pattern.compile(
//            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
//        )
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text

        fun isValidString(str: String): Boolean{
            return Patterns.EMAIL_ADDRESS.matcher(str).matches()
        }

        signupButton.setOnClickListener {
            if (!isValidString(email.toString())) {
                Toast.makeText(
                this@SignupActivity,
                "invalid email format $email!",
                Toast.LENGTH_SHORT
            ).show()
                return@setOnClickListener
            }
            Toast.makeText(
                this@SignupActivity,
                "successfully signed up $email!",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent) // Close the current Activity

        }
    }
}
