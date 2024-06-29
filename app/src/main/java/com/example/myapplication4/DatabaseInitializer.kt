package com.example.myapplication4

import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Card
import com.example.myapplication4.data.Column

suspend fun initializeDatabase(database: AppDatabase) {
    val columnDao = database.columnDao()
    val cardDao = database.cardDao()

    val columns = listOf(
        Column(title = "Column 1", description = "Description 1", sortOrder = 1),
        Column(title = "Column 2", description = "Description 2", sortOrder = 2),
        Column(title = "Column 3", description = "Description 3", sortOrder = 3)
    )

    columns.forEach { column ->
        val columnId = columnDao.insertColumn(column).toInt()
        val cards = listOf(
            Card(columnId = columnId, title = "Card 1", description = "Description 1", sortOrder = 1),
            Card(columnId = columnId, title = "Card 2", description = "Description 2", sortOrder = 2),
            Card(columnId = columnId, title = "Card 3", description = "Description 3", sortOrder = 3),
            Card(columnId = columnId, title = "Card 4", description = "Description 4", sortOrder = 4),
            Card(columnId = columnId, title = "Card 5", description = "Description 5", sortOrder = 5)
        )
        cards.forEach { card -> cardDao.insertCard(card) }
    }
}
