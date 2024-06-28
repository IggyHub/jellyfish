package com.example.myapplication4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Card
import com.example.myapplication4.data.Column
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        database = AppDatabase.getDatabase(this)

        runBlocking {
            initializeDatabase(database)
        }

        setContent {
            MyApp(database)
        }
    }
}

suspend fun initializeDatabase(database: AppDatabase) {
    val columnDao = database.columnDao()
    val cardDao = database.cardDao()

    val columns = listOf(
        Column(title = "Column 1", description = "Description 1", sortOrder = 1),
        Column(title = "Column 2", description = "Description 2", sortOrder = 2)
    )

    columns.forEach { column ->
        val columnId = columnDao.insertColumn(column).toInt()
        val cards = listOf(
            Card(columnId = columnId, title = "Card 1", description = "Description 1", sortOrder = 1),
            Card(columnId = columnId, title = "Card 2", description = "Description 2", sortOrder = 2)
        )
        cards.forEach { card -> cardDao.insertCard(card) }
    }
}

@Composable
fun MyApp(database: AppDatabase) {
    val columns = remember { mutableStateListOf<Column>() }
    val cards = remember { mutableStateMapOf<Int, List<Card>>() }

    LaunchedEffect(Unit) {
        val fetchedColumns = database.columnDao().getAllColumns()
        columns.addAll(fetchedColumns)
        fetchedColumns.forEach { column ->
            cards[column.id] = database.cardDao().getCardsForColumn(column.id)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(columns.size) { index ->
            val column = columns[index]
            ColumnView(column, cards[column.id] ?: emptyList())
        }
    }
}

@Composable
fun ColumnView(column: Column, cards: List<Card>) {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = column.title, style = MaterialTheme.typography.h6)
        Text(text = column.description, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        cards.forEach { card ->
            Text(
                text = card.title,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = card.description,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
