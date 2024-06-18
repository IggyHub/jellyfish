package com.example.myapplication4.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {
    @Insert
    suspend fun insert(profile: Profile)

    @Query("SELECT * FROM profile WHERE id = :id")
    suspend fun getProfile(id: Int): Profile?

    @Query("SELECT * FROM profile ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastUpdatedProfile(): Profile?
}
