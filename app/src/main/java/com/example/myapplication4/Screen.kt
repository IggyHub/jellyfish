package com.example.myapplication4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication4.data.AppDatabase
import com.example.myapplication4.data.Column
import com.example.myapplication4.data.Card
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(database: AppDatabase) {
    val columns = remember { mutableStateListOf<Column>() }
    val cards = remember { mutableStateMapOf<Int, List<Card>>() }
    val coroutineScope = rememberCoroutineScope()

    // Function to refresh columns and cards
    val refreshData = {
        coroutineScope.launch {
            val fetchedColumns = database.columnDao().getAllColumns()
            columns.clear()
            columns.addAll(fetchedColumns)
            fetchedColumns.forEach { column ->
                cards[column.id] = database.cardDao().getCardsForColumn(column.id)
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(columns) { column ->
            ColumnView(
                database = database,
                column = column,
                cards = cards[column.id] ?: emptyList(),
                backgroundColor = Color.LightGray,
                onColumnDeleted = { refreshData() },
                onCardsDeleted = { refreshData() }
            )
        }
    }
}

@Composable
fun DashboardScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Green)) {
        Text("Dashboard Screen", style = MaterialTheme.typography.h4, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Blue)) {
        Text("Settings Screen", style = MaterialTheme.typography.h4, modifier = Modifier.padding(16.dp))
    }
}
