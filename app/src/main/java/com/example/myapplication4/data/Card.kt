package com.example.myapplication4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "columns")
data class Column(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val sortOrder: Int
)

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val columnId: Int,
    val title: String,
    val description: String,
    val sortOrder: Int
)
