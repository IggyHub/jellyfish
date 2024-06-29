package com.example.myapplication4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication4.data.AppDatabase
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        database = AppDatabase.getDatabase(this)

        var bTestInit = false

        if (bTestInit) {
            runBlocking {
                initializeDatabase(database)
            }
        }

        setContent {
            MyApp(database)
        }
    }
}
