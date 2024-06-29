package com.example.myapplication4.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface ColumnDao {
    @Insert
    suspend fun insertColumn(column: Column): Long

    @Query("SELECT * FROM columns ORDER BY sortOrder")
    suspend fun getAllColumns(): List<Column>

    @Delete
    suspend fun deleteColumn(column: Column)
}

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: Card)

    @Query("SELECT * FROM cards WHERE columnId = :columnId ORDER BY sortOrder")
    suspend fun getCardsForColumn(columnId: Int): List<Card>

    @Query("DELETE FROM cards WHERE columnId = :columnId")
    suspend fun deleteCardsByColumn(columnId: Int)
}
