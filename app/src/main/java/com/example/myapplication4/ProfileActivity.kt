package com.example.myapplication4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Profile
import com.example.myapplication4.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Enable dark mode based on system settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Initialize database
        database = AppDatabase.getDatabase(this)

        // Load profile data from database
        loadProfile()

        // Set up the save button click listener
        binding.saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            val profile = database.profileDao().getLastUpdatedProfile()
            profile?.let {
                binding.nameEditText.setText(it.name)
                binding.aboutEditText.setText(it.about)
                binding.yearOfBirthEditText.setText(it.yearOfBirth.toString())
            }
        }
    }

    private fun saveProfile() {
        val name = binding.nameEditText.text.toString()
        val about = binding.aboutEditText.text.toString()
        val yearOfBirth = binding.yearOfBirthEditText.text.toString().toIntOrNull()

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
