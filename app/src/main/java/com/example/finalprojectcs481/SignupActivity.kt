package com.example.finalprojectcs481

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.finalprojectcs481.fragments.HomeFragment
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance() // Initialize Firebase Authentication

        val backButton = findViewById<Button>(R.id.backButton)
        val signupButton = findViewById<Button>(R.id.submit_button)

        backButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Close the current SignupActivity
        }

        signupButton.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
//        val confirmPassword = view.findViewById<EditText>(R.id.confirmPassword).text.toString()
//
//        if (password != confirmPassword) {
//            view.findViewById<EditText>(R.id.confirmPassword).error = "Passwords do not match!"
//            return
//        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, now you can save other user data to Firestore if needed
                    val user: MutableMap<String, Any> = HashMap()
                    user["email"] = email
                    user["firstname"] = findViewById<EditText>(R.id.firstName).text.toString()
                    user["lastname"] = findViewById<EditText>(R.id.lastName).text.toString()
                    user["username"] = findViewById<EditText>(R.id.username).text.toString()
                    // Do NOT store the password here

                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            Log.d("dbFirebase", "User profile created for ID: ${auth.currentUser!!.uid}")

                            val intent = Intent(this, WelcomeActivity::class.java)
                            startActivity(intent)
                            finish() // Close the current SignupActivity
                            Toast.makeText(this@SignupActivity, "Account Creation Successful!", Toast.LENGTH_SHORT).show()


                        }
                        .addOnFailureListener {
                            Log.d("dbFirebase", "Error adding user profile", it)
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignupActivity, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()

                }
            }
    }
}
