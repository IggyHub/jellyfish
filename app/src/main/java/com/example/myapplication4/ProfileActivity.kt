package com.example.myapplication4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var aboutEditText: EditText
    private lateinit var yearOfBirthEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        aboutEditText = findViewById(R.id.aboutEditText)
        yearOfBirthEditText = findViewById(R.id.yearOfBirthEditText)
        saveButton = findViewById(R.id.saveButton)

        // Set up the save button click listener
        saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = nameEditText.text.toString()
        val about = aboutEditText.text.toString()
        val yearOfBirth = yearOfBirthEditText.text.toString()

        // Here you can save the data to a database or use it in some other way
        // For now, let's just log it
        android.util.Log.d("ProfileActivity", "Saving profile: Name: $name, About: $about, Year of Birth: $yearOfBirth")
    }
}
