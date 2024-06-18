package com.example.myapplication4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Profile
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var aboutEditText: EditText
    private lateinit var yearOfBirthEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Enable dark mode based on system settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        aboutEditText = findViewById(R.id.aboutEditText)
        yearOfBirthEditText = findViewById(R.id.yearOfBirthEditText)
        saveButton = findViewById(R.id.saveButton)

        // Initialize database
        database = AppDatabase.getDatabase(this)

        // Load profile data from database
        loadProfile()

        // Set up the save button click listener
        saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            val profile = database.profileDao().getLastUpdatedProfile()
            profile?.let {
                nameEditText.setText(it.name)
                aboutEditText.setText(it.about)
                yearOfBirthEditText.setText(it.yearOfBirth.toString())
            }
        }
    }

    private fun saveProfile() {
        val name = nameEditText.text.toString()
        val about = aboutEditText.text.toString()
        val yearOfBirth = yearOfBirthEditText.text.toString().toIntOrNull()

        if (yearOfBirth != null) {
            val profile = Profile(
                name = name,
                about = about,
                yearOfBirth = yearOfBirth,
                lastUpdated = System.currentTimeMillis()
            )

            lifecycleScope.launch {
                database.profileDao().insert(profile)
                android.util.Log.d("ProfileActivity", "Profile saved: $profile")
            }
        } else {
            android.util.Log.e("ProfileActivity", "Year of birth must be a number")
        }
    }
}
