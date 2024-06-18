package com.example.myapplication4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.content.Intent
import com.example.myapplication4.databinding.ActivityMainBinding


import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Card

import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable dark mode based on system settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonGoToProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        database = AppDatabase.getDatabase(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Load data from the database
        loadCards()
    }
    private fun loadCards() {
        lifecycleScope.launch {
            val cards = database.cardDao().getAllCards()
            adapter = CardAdapter(cards)
            binding.recyclerView.adapter = adapter
        }
    }
}