package com.example.pick_a_flick

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // make variables and connect them to the elements in the app
        val input: EditText = findViewById(R.id.usernameField)
        val proceedBtn: ImageButton = findViewById(R.id.proceedBtn)

        val userNameSP: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // func to save inputted value to shared prefs
        proceedBtn.setOnClickListener {
            val userInput = input.text.toString() // get the value from user

            if (userInput.isNotEmpty()) {
                val editor = userNameSP.edit()
                editor.putString("text", userInput)
                editor.apply()

                // Go to MainActivity after saving the name
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // optional: removes Welcome screen from back stack
            } else {
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}