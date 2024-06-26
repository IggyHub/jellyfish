package com.example.myapplication4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//import com.example.myapplication4.data.Card

@Database(entities = [Card::class, Profile::class], version = 2, exportSchema = false) // Incremented version number
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration() // This will reset the database if the schema is changed, use with caution
                    .build()
                INSTANCE = instance
                instance
             }
        }
    }
}
