package com.example.myapplication4.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val about: String,
    val yearOfBirth: Int,
    val lastUpdated: Long = System.currentTimeMillis() // Store the timestamp in milliseconds
)
