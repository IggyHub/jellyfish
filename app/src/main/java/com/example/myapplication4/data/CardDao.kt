package com.example.myapplication4.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<Card>

    @Insert
    suspend fun insert(card: Card)
}
