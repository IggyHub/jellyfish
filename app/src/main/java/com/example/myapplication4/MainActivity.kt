package com.example.myapplication4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(columns) { column ->
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
            .width(300.dp)
            .fillMaxHeight() // Ensure column fills the height of the parent
    ) {
        Text(text = column.title, style = MaterialTheme.typography.h6)
        Text(text = column.description, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxHeight() // Ensure LazyColumn fills the height of the parent
        ) {
            items(cards) { card ->
                CardView(card)
            }
        }
    }
}

@Composable
fun CardView(card: Card) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(text = card.title)
        Text(text = card.description)
    }
}
